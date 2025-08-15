package com.challenge.adapters.http.simulation

import com.challenge.adapters.http.simulation.request.SimulateCreditInBatchRequest
import com.challenge.adapters.http.simulation.request.SimulationRequest
import com.challenge.adapters.http.simulation.request.toMonetaryAmount
import com.challenge.adapters.http.simulation.request.toSimulationRequest
import com.challenge.adapters.http.simulation.response.SimulateCreditInBatchResponse
import com.challenge.adapters.http.simulation.response.toResponse
import com.challenge.creditsimulator.application.simulation.command.SimulationBatchCommand
import com.challenge.creditsimulator.application.simulation.command.SimulationBatchCommandHandler
import com.challenge.creditsimulator.application.simulation.command.SingleSimulationCommand
import com.challenge.creditsimulator.application.simulation.command.SingleSimulationCommandHandler
import com.challenge.creditsimulator.application.simulation.query.SimulationsByRequestIdQuery
import com.challenge.creditsimulator.application.simulation.query.SimulationsByRequestIdQueryHandler
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.UUID

class SimulationHandler(
    private val singleSimulationCommandHandler: SingleSimulationCommandHandler,
    private val simulationBatchCommandHandler: SimulationBatchCommandHandler,
    private val simulationsByRequestIdQueryHandler: SimulationsByRequestIdQueryHandler,
) {
    suspend fun simulateSingleCredit(req: ServerRequest): ServerResponse {
        // TODO: Criar validacoes nos parametros recebidos no body
        val body = req.awaitBody<SimulationRequest>()
        val result =
            singleSimulationCommandHandler.handle(
                SingleSimulationCommand(
                    birthdate = body.birthdate,
                    amountOfMonths = body.amountOfMonths,
                    loanAmount = body.loanAmount.toMonetaryAmount(),
                ),
            )
        return ServerResponse.ok().bodyValueAndAwait(result.toResponse())
    }

    suspend fun simulateCreditInBatch(req: ServerRequest): ServerResponse {
        // TODO: Criar validacoes nos parametros recebidos no body
        val body = req.awaitBody<SimulateCreditInBatchRequest>()
        val result =
            simulationBatchCommandHandler.handle(
                SimulationBatchCommand(
                    simulations =
                        body.batch
                            .parallelStream()
                            .map { it.toSimulationRequest() }
                            .toList(),
                ),
            )
        return ServerResponse
            .ok()
            .bodyValueAndAwait(
                SimulateCreditInBatchResponse(result.toString()),
            )
    }

    suspend fun findByRequestId(req: ServerRequest): ServerResponse {
        val requestId = req.pathVariable("requestId")
        val offset = req.queryParam("offset").orElse("0").toInt()
        val limit = req.queryParam("limit").orElse("5").toInt()

        return ServerResponse.ok().bodyValueAndAwait(
            simulationsByRequestIdQueryHandler.handle(
                query =
                    SimulationsByRequestIdQuery(
                        requestId = UUID.fromString(requestId),
                        offset = offset,
                        limit = limit,
                    ),
            ),
        )
    }
}
