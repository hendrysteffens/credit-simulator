package com.challenge.adapters.http

import com.challenge.adapters.http.simulation.SimulationHandler
import com.challenge.adapters.http.simulation.response.SimulateCreditInBatchResponse
import com.challenge.adapters.http.simulation.response.SimulationResponse
import com.challenge.adapters.http.simulation.response.toResponse
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import java.util.UUID

class RouterTest :
    DescribeSpec({

        lateinit var webTestClient: WebTestClient
        val simulationHandler = mockk<SimulationHandler>()
        val fixture = RouterTestFixture

        beforeSpec {
            webTestClient =
                WebTestClient
                    .bindToRouterFunction(router(simulationHandler))
                    .configureClient()
                    .build()
        }

        describe("POST /v1/credit/simulation") {
            context("with valid request") {
                it("should return 200 and simulation result") {
                    val simulation = fixture.createSampleSimulationWithValues()
                    coEvery {
                        simulationHandler.simulateSingleCredit(any())
                    } returns ServerResponse.ok().bodyValueAndAwait(simulation.toResponse())

                    webTestClient
                        .post()
                        .uri("/v1/credit/simulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(fixture.validRequestBody())
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectBody<SimulationResponse>()
                        .returnResult()
                        .responseBody shouldBe simulation.toResponse()
                }
            }

            // TODO: Criar validacoes na request
            xcontext("with invalid request") {
            }

            context("when service fails") {
                it("should return 500 for internal server error") {
                    coEvery { simulationHandler.simulateSingleCredit(any()) } throws RuntimeException("Internal error")

                    webTestClient
                        .post()
                        .uri("/v1/credit/simulation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(fixture.validRequestBody())
                        .exchange()
                        .expectStatus()
                        .is5xxServerError
                }
            }
        }
        describe("Batch Credit Simulation") {
            context("POST /batch") {
                it("should return 200 and requestId for valid batch request") {
                    val requestId = UUID.randomUUID().toString()
                    coEvery {
                        simulationHandler.simulateCreditInBatch(any())
                    } returns ServerResponse.ok().bodyValueAndAwait(SimulateCreditInBatchResponse(requestId))

                    webTestClient
                        .post()
                        .uri("/v1/credit/simulation/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(fixture.validBatchSimulationRequest())
                        .exchange()
                        .expectStatus()
                        .isOk
                        .expectBody<SimulateCreditInBatchResponse>()
                        .returnResult()
                        .responseBody shouldBe SimulateCreditInBatchResponse(requestId)
                }

                it("should return 500 when batch service fails") {
                    coEvery { simulationHandler.simulateCreditInBatch(any()) } throws RuntimeException("Batch error")

                    webTestClient
                        .post()
                        .uri("/v1/credit/simulation/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(fixture.validBatchSimulationRequest())
                        .exchange()
                        .expectStatus()
                        .is5xxServerError
                }
            }
        }
    })
