package com.challenge.creditsimulator.domain.simulation

import com.challenge.creditsimulator.domain.simulation.SimulationTestFixture.scenarioAge25Loan1000x12
import com.challenge.creditsimulator.domain.simulation.SimulationTestFixture.scenarioAge35Loan1000x12
import com.challenge.creditsimulator.domain.simulation.SimulationTestFixture.scenarioAge55Loan1000x12
import com.challenge.creditsimulator.domain.simulation.SimulationTestFixture.scenarioAge75Loan1000x12
import com.challenge.creditsimulator.domain.toBigDecimal
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class SimulationTest :
    DescribeSpec({

        describe("Simulation Test") {
            context("Success cases") {
                it("Should create a simulation with 5% a.a. when age is 25") {
                    val (simulation, expected) = scenarioAge25Loan1000x12()
                    expected.monthlyPayment shouldBe simulation.monthlyPayment.toBigDecimal()
                    expected.totalAmount shouldBe simulation.totalAmount.toBigDecimal()
                    expected.totalInterest shouldBe simulation.totalInterest.toBigDecimal()
                }

                it("Should create a simulation with 3% a.a. when age is 35") {
                    val (simulation, expected) = scenarioAge35Loan1000x12()
                    expected.monthlyPayment shouldBe simulation.monthlyPayment.toBigDecimal()
                    expected.totalAmount shouldBe simulation.totalAmount.toBigDecimal()
                    expected.totalInterest shouldBe simulation.totalInterest.toBigDecimal()
                }

                it("Should create a simulation with 2% a.a. when age is 55") {
                    val (simulation, expected) = scenarioAge55Loan1000x12()
                    expected.monthlyPayment shouldBe simulation.monthlyPayment.toBigDecimal()
                    expected.totalAmount shouldBe simulation.totalAmount.toBigDecimal()
                    expected.totalInterest shouldBe simulation.totalInterest.toBigDecimal()
                }

                it("Should create a simulation with 4% a.a. when age is 75") {
                    val (simulation, expected) = scenarioAge75Loan1000x12()
                    expected.monthlyPayment shouldBe simulation.monthlyPayment.toBigDecimal()
                    expected.totalAmount shouldBe simulation.totalAmount.toBigDecimal()
                    expected.totalInterest shouldBe simulation.totalInterest.toBigDecimal()
                }
            }
        }
    })
