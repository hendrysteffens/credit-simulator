package com.challenge.adapters.http

import com.challenge.adapters.http.simulation.SimulationHandler
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter

private const val UUID_REGEX = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}"

fun router(simulationHandler: SimulationHandler) =
    coRouter {
        accept(APPLICATION_JSON).nest {
            "/v1/credit/simulation".nest {
                POST("", simulationHandler::simulateSingleCredit)
                POST("/batch", simulationHandler::simulateCreditInBatch)
                GET("/{requestId:${UUID_REGEX}}", simulationHandler::findByRequestId)
            }
        }
    }

fun swaggerRoutes() =
    coRouter {
        // Servir o JSON da especificação
        GET("/openapi.json", ::serveOpenApiJson)
    }

private suspend fun serveOpenApiJson(request: ServerRequest): ServerResponse {
    val resource = ClassPathResource("static/openapi.json")
    return ServerResponse
        .ok()
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValueAndAwait(resource.inputStream.readAllBytes().decodeToString())
}
