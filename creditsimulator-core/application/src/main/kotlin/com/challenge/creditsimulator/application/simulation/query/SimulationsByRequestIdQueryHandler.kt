package com.challenge.creditsimulator.application.simulation.query

import com.challenge.creditsimulator.domain.port.SimulationRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SimulationsByRequestIdQueryHandler(
    private val simulationRepository: SimulationRepository,
) {
    suspend fun handle(query: SimulationsByRequestIdQuery): SimulationsByRequestIdProjection {
        return coroutineScope {
            val simulations =
                async {
                    simulationRepository.findSimulationPageByRequestId(
                        requestId = query.requestId,
                        offset = query.offset,
                        limit = query.limit,
                    )
                }
            val totalSimulations =
                async {
                    simulationRepository.getTotalSimulationsByRequestId(query.requestId)
                }

            return@coroutineScope SimulationsByRequestIdProjection(
                simulations = simulations.await().map { it.toSimulationResult() }.toImmutableList(),
                totalItems = totalSimulations.await(),
                totalPages = totalSimulations.await() / query.limit + 1,
                offset = query.offset,
                limit = query.limit,
            )
        }
    }
}
