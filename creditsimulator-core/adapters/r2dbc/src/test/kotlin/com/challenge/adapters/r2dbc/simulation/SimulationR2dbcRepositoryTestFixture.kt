package com.challenge.adapters.r2dbc.simulation

import com.challenge.creditsimulator.domain.createBirthdateForAge
import com.challenge.creditsimulator.domain.simulation.Birthdate
import com.challenge.creditsimulator.domain.simulation.Simulation
import com.challenge.creditsimulator.domain.withCurrency
import java.math.BigDecimal
import javax.money.MonetaryAmount

object SimulationR2dbcRepositoryTestFixture {
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

    fun scenarioAge25Loan1000x12(): Simulation {
        val simulation =
            createSimulation(
                birthdate = createBirthdateForAge(25),
                loanAmount = BigDecimal("1000.00").withCurrency("BRL"),
                annualRate = BigDecimal("0.05"),
                months = 12,
            )
        return simulation
    }

    fun scenarioAge35Loan1000x12(): Simulation {
        val simulation =
            createSimulation(
                birthdate = createBirthdateForAge(35),
                loanAmount = BigDecimal("1000.00").withCurrency("BRL"),
                annualRate = BigDecimal("0.02"),
                months = 12,
            )
        return simulation
    }

    fun deleteAll() = "DELETE FROM simulation WHERE 1=1"
}
