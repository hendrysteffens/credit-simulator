package com.challenge.creditsimulator.application.simulation

import com.challenge.creditsimulator.application.simulation.command.SingleSimulationCommand
import com.challenge.creditsimulator.domain.createBirthdateForAge
import com.challenge.creditsimulator.domain.withCurrency
import java.math.BigDecimal
import javax.money.Monetary

object SingleSimulationCommandHandlerTestFixture {
    fun defaultMonths() = 12

    fun defaultLoanAmount() = BigDecimal("1000.00").withCurrency("BRL")

    fun brazilianCurrency() = Monetary.getCurrency("BRL")

    fun usdCurrency() = Monetary.getCurrency("USD")

    fun createCommand(
        age: Int,
        amountOfMonths: Int = 12,
        loanAmount: String = "1000.00",
        currency: String = "BRL",
    ): SingleSimulationCommand =
        SingleSimulationCommand(
            birthdate = createBirthdateForAge(age),
            amountOfMonths = amountOfMonths,
            loanAmount = BigDecimal(loanAmount).withCurrency(currency),
        )

    fun simulationAge35Loan1000x12And3Percent() =
        ExpectedValues(
            monthlyPayment = BigDecimal("84.69"),
            totalAmount = BigDecimal("1016.28"),
            totalInterest = BigDecimal("16.28"),
        )

    data class ExpectedValues(
        val monthlyPayment: BigDecimal,
        val totalAmount: BigDecimal,
        val totalInterest: BigDecimal,
    )
}
