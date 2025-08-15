package com.challenge.creditsimulator.application.simulation

import com.challenge.creditsimulator.application.simulation.SingleSimulationCommandHandlerTestFixture.brazilianCurrency
import com.challenge.creditsimulator.application.simulation.SingleSimulationCommandHandlerTestFixture.createCommand
import com.challenge.creditsimulator.application.simulation.SingleSimulationCommandHandlerTestFixture.defaultLoanAmount
import com.challenge.creditsimulator.application.simulation.SingleSimulationCommandHandlerTestFixture.defaultMonths
import com.challenge.creditsimulator.application.simulation.SingleSimulationCommandHandlerTestFixture.simulationAge35Loan1000x12And3Percent
import com.challenge.creditsimulator.application.simulation.SingleSimulationCommandHandlerTestFixture.usdCurrency
import com.challenge.creditsimulator.application.simulation.command.SingleSimulationCommandHandler
import com.challenge.creditsimulator.domain.createBirthdateForAge
import com.challenge.creditsimulator.domain.port.InterestRatePolicy
import com.challenge.creditsimulator.domain.toBigDecimal
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.math.BigDecimal

class SingleSimulationCommandHandlerTest :
    DescribeSpec({

        val mockInterestRatePolicy = mockk<InterestRatePolicy>()
        val handler = SingleSimulationCommandHandler(mockInterestRatePolicy)

        beforeEach {
            clearAllMocks()
        }

        describe("SingleSimulationCommandHandler") {
            context("Success cases") {

                it("should create simulation with correct parameters for age 30 (3%)") {
                    val birthdate = createBirthdateForAge(30)
                    every {
                        mockInterestRatePolicy.getAnnualRate(birthdate)
                    } returns BigDecimal("0.03")

                    val command = createCommand(age = 30)
                    val simulation = handler.handle(command)

                    simulation.birthdate shouldBe birthdate
                    simulation.loanAmount shouldBe defaultLoanAmount()
                    simulation.amountOfMonths shouldBe defaultMonths()
                    simulation.monthlyPayment.currency shouldBe brazilianCurrency()
                    simulation.totalAmount.currency shouldBe brazilianCurrency()
                    simulation.totalInterest.currency shouldBe brazilianCurrency()
                    simulation.totalInterest.toBigDecimal() shouldBe simulationAge35Loan1000x12And3Percent().totalInterest
                    simulation.totalAmount.toBigDecimal() shouldBe simulationAge35Loan1000x12And3Percent().totalAmount
                    simulation.monthlyPayment.toBigDecimal() shouldBe simulationAge35Loan1000x12And3Percent().monthlyPayment

                    verify(exactly = 1) { mockInterestRatePolicy.getAnnualRate(birthdate) }
                }

                it("should generate different UUIDs for request and simulation") {
                    val birthdate = createBirthdateForAge(30)
                    every { mockInterestRatePolicy.getAnnualRate(birthdate) } returns BigDecimal("0.03")

                    val command = createCommand(age = 30)

                    val simulation1 = handler.handle(command)
                    val simulation2 = handler.handle(command)

                    simulation1.simulationId shouldNotBe simulation2.simulationId
                    simulation1.requestId shouldNotBe simulation2.requestId
                    simulation1.simulationId shouldNotBe simulation1.requestId

                    verify(exactly = 2) { mockInterestRatePolicy.getAnnualRate(birthdate) }
                }

                it("should handle different currencies") {
                    val birthdate = createBirthdateForAge(30)
                    val usdCommand = createCommand(age = 30, currency = "USD")
                    every { mockInterestRatePolicy.getAnnualRate(birthdate) } returns BigDecimal("0.03")

                    val simulation = handler.handle(usdCommand)

                    simulation.loanAmount.currency shouldBe usdCurrency()
                    simulation.totalAmount.currency shouldBe usdCurrency()
                    simulation.monthlyPayment.currency shouldBe usdCurrency()
                }
            }
        }
        context("should handle edge cases") {
            it("should create simulation for 1 month") {
                val shortTermCommand = createCommand(age = 30, amountOfMonths = 1)
                val birthdate = createBirthdateForAge(30)
                every {
                    mockInterestRatePolicy.getAnnualRate(birthdate)
                } returns BigDecimal("0.03")

                val simulation = handler.handle(shortTermCommand)
                simulation.monthlyPayment.toBigDecimal() shouldBe BigDecimal("1002.5")

                verify(exactly = 1) { mockInterestRatePolicy.getAnnualRate(birthdate) }
            }

            it("should throw exception for invalid months") {
                val birthdate = createBirthdateForAge(30)
                val invalidCommand = createCommand(age = 30, amountOfMonths = 0)
                every {
                    mockInterestRatePolicy.getAnnualRate(birthdate)
                } returns BigDecimal("0.03")
                shouldThrow<Exception> {
                    handler.handle(invalidCommand)
                }
            }
        }
    })
