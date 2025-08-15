package com.challenge.creditsimulator.domain.simulation

import com.challenge.creditsimulator.domain.simulation.Status.COMPLETED
import com.challenge.creditsimulator.domain.simulation.Status.PENDING
import com.challenge.creditsimulator.domain.toBigDecimal
import com.challenge.creditsimulator.domain.withCurrency
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.util.UUID
import javax.money.MonetaryAmount

private val Simulation.Companion.MONTHS_IN_YEAR: BigDecimal
    get() = BigDecimal.valueOf(12)
private val Simulation.Companion.INTERMEDIATE_SCALE: Int
    get() = 15
private val Simulation.Companion.FINAL_SCALE: Int
    get() = 2

data class Simulation(
    val simulationId: UUID,
    val requestId: UUID,
    val status: Status = PENDING,
    val birthdate: Birthdate,
    val amountOfMonths: Int,
    val loanAmount: MonetaryAmount,
    val totalAmount: MonetaryAmount,
    val monthlyPayment: MonetaryAmount,
    val totalInterest: MonetaryAmount,
    val errorMessage: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
) {
    companion object {
        fun create(
            requestId: UUID = UUID.randomUUID(),
            birthdate: Birthdate,
            loanAmount: MonetaryAmount,
            annualRate: BigDecimal,
            months: Int,
        ): Simulation {
            val monthlyPayment = calculateInstallment(loanAmount.toBigDecimal(), annualRate, months)
            val totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(months.toLong()))
            val totalInterest = totalAmount.subtract(loanAmount.toBigDecimal())

            return Simulation(
                simulationId = UUID.randomUUID(),
                requestId = requestId,
                status = COMPLETED,
                birthdate = birthdate,
                loanAmount = loanAmount,
                amountOfMonths = months,
                totalAmount = totalAmount.withCurrency(loanAmount.currency),
                monthlyPayment = monthlyPayment.withCurrency(loanAmount.currency),
                totalInterest = totalInterest.withCurrency(loanAmount.currency),
                createdAt = Clock.System.now(),
            )
        }

        private fun calculateInstallment(
            loanAmount: BigDecimal,
            annualRate: BigDecimal,
            months: Int,
        ): BigDecimal {
            // Taxa de juros mensal
            val monthlyRate =
                annualRate
                    .divide(MONTHS_IN_YEAR, INTERMEDIATE_SCALE, RoundingMode.HALF_EVEN)

            // Fator de crescimento dos juros no prazo total
            val interestFactor =
                BigDecimal.ONE
                    .add(monthlyRate)
                    .pow(
                        months,
                        MathContext.DECIMAL128,
                    )

            // Valor ajustado com juros no período
            val adjustedLoanValue =
                loanAmount
                    .multiply(monthlyRate)
                    .multiply(interestFactor)

            // Fator para calcular a parcela fixa
            val installmentFactor = interestFactor.subtract(BigDecimal.ONE)

            // Parcela mensal
            return adjustedLoanValue
                .divide(installmentFactor, FINAL_SCALE, RoundingMode.HALF_EVEN)
        }
    }
}

typealias Birthdate = LocalDate

enum class Status {
    PENDING,
    COMPLETED,
    FAILED,
}
