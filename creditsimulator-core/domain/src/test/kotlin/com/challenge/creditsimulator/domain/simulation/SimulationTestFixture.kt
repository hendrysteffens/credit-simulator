package com.challenge.creditsimulator.domain.simulation

import com.challenge.creditsimulator.domain.createBirthdateForAge
import com.challenge.creditsimulator.domain.withCurrency
import java.math.BigDecimal
import javax.money.MonetaryAmount

object SimulationTestFixture {
    fun createSimulation(
        birthdate: Birthdate,
        loanAmount: MonetaryAmount,
        annualRate: BigDecimal,
        months: Int,
    ): Simulation =
        Simulation.create(
            birthdate = birthdate,
            loanAmount = loanAmount,
            annualRate = annualRate,
            months = months,
        )

    fun scenarioAge25Loan1000x12(): Pair<Simulation, ExpectedValues> {
        val simulation =
            createSimulation(
                birthdate = createBirthdateForAge(25),
                loanAmount = BigDecimal("1000.00").withCurrency("BRL"),
                annualRate = BigDecimal("0.05"),
                months = 12,
            )
        return simulation to
            ExpectedValues(
                monthlyPayment = BigDecimal("85.61"),
                totalAmount = BigDecimal("1027.32"),
                totalInterest = BigDecimal("27.32"),
            )
    }

    fun scenarioAge35Loan1000x12(): Pair<Simulation, ExpectedValues> {
        val simulation =
            createSimulation(
                birthdate = createBirthdateForAge(35),
                loanAmount = BigDecimal("1000.00").withCurrency("BRL"),
                annualRate = BigDecimal("0.03"),
                months = 12,
            )
        return simulation to
            ExpectedValues(
                monthlyPayment = BigDecimal("84.69"),
                totalAmount = BigDecimal("1016.28"),
                totalInterest = BigDecimal("16.28"),
            )
    }

    fun scenarioAge55Loan1000x12(): Pair<Simulation, ExpectedValues> {
        val simulation =
            createSimulation(
                birthdate = createBirthdateForAge(55),
                loanAmount = BigDecimal("1000.00").withCurrency("BRL"),
                annualRate = BigDecimal("0.02"),
                months = 12,
            )
        return simulation to
            ExpectedValues(
                monthlyPayment = BigDecimal("84.24"),
                totalAmount = BigDecimal("1010.88"),
                totalInterest = BigDecimal("10.88"),
            )
    }

    fun scenarioAge75Loan1000x12(): Pair<Simulation, ExpectedValues> {
        val simulation =
            createSimulation(
                birthdate = createBirthdateForAge(75),
                loanAmount = BigDecimal("1000.00").withCurrency("BRL"),
                annualRate = BigDecimal("0.04"),
                months = 12,
            )
        return simulation to
            ExpectedValues(
                monthlyPayment = BigDecimal("85.15"),
                totalAmount = BigDecimal("1021.8"),
                totalInterest = BigDecimal("21.8"),
            )
    }

    data class ExpectedValues(
        val monthlyPayment: BigDecimal,
        val totalAmount: BigDecimal,
        val totalInterest: BigDecimal,
    )
}
