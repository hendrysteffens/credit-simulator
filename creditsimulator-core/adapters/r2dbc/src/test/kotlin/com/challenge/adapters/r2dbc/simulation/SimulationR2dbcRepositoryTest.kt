package com.challenge.adapters.r2dbc.simulation

import com.challenge.adapters.r2dbc.simulation.DatabaseClientFixture.databaseClient
import com.challenge.adapters.r2dbc.simulation.SimulationR2dbcRepositoryTestFixture.deleteAll
import com.challenge.adapters.r2dbc.simulation.SimulationR2dbcRepositoryTestFixture.scenarioAge25Loan1000x12
import com.challenge.adapters.r2dbc.simulation.SimulationR2dbcRepositoryTestFixture.scenarioAge35Loan1000x12
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import org.springframework.r2dbc.core.await

class SimulationR2dbcRepositoryTest :
    DescribeSpec({

        val db = databaseClient()
        val repository = SimulationR2dbcRepository(db)

        beforeEach {
            db.sql(deleteAll()).await()
        }

        describe("SimulationR2dbcRepository") {
            context("Create simulation") {
                it("Should create and find by id") {
                    val simulation = scenarioAge25Loan1000x12()
                    repository.save(simulation)
                    val result =
                        repository
                            .findSimulationPageByRequestId(simulation.requestId, 0, 1)
                    with(result[0]) {
                        this.requestId shouldBe simulation.requestId
                        this.totalInterest shouldBe simulation.totalInterest
                        this.monthlyPayment shouldBe simulation.monthlyPayment
                        this.totalAmount shouldBe simulation.totalAmount
                        this.loanAmount shouldBe simulation.loanAmount
                        this.amountOfMonths shouldBe simulation.amountOfMonths
                    }
                }

                it("Should create two simulation and find both by requestId") {
                    val simulation = scenarioAge25Loan1000x12()
                    val simulation2 = scenarioAge35Loan1000x12().copy(requestId = simulation.requestId)

                    repository.save(simulation)
                    repository.save(simulation2)
                    val result =
                        repository
                            .findSimulationPageByRequestId(simulation.requestId, 0, 5)

                    result.size shouldBe 2
                    with(result[0]) {
                        this.requestId shouldBe simulation.requestId
                        this.totalInterest shouldBe simulation.totalInterest
                        this.monthlyPayment shouldBe simulation.monthlyPayment
                        this.totalAmount shouldBe simulation.totalAmount
                        this.loanAmount shouldBe simulation.loanAmount
                        this.amountOfMonths shouldBe simulation.amountOfMonths
                    }
                    with(result[1]) {
                        this.requestId shouldBe simulation2.requestId
                        this.totalInterest shouldBe simulation2.totalInterest
                        this.monthlyPayment shouldBe simulation2.monthlyPayment
                        this.totalAmount shouldBe simulation2.totalAmount
                        this.loanAmount shouldBe simulation2.loanAmount
                        this.amountOfMonths shouldBe simulation2.amountOfMonths
                    }
                }
            }
        }
    })
