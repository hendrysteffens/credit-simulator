package com.challenge.creditsimulator.application.simulation.query

import com.challenge.creditsimulator.domain.port.SavedSimulation
import com.challenge.creditsimulator.domain.toBigDecimal
import kotlinx.collections.immutable.ImmutableList
import java.math.BigDecimal
import javax.money.MonetaryAmount

data class SimulationsByRequestIdProjection(
    val simulations: ImmutableList<SimulationResult>,
    val totalPages: Int,
    val totalItems: Int,
    val offset: Int,
    val limit: Int,
) {
    data class SimulationResult(
        val birthdate: String,
        val amountOfMonths: Int,
        val loanAmount: Amount,
        val totalAmount: Amount,
        val monthlyPayment: Amount,
        val totalInterest: Amount,
        val createdAt: Long,
    )

    data class Amount(
        val amount: BigDecimal,
        val currency: String,
    )
}

fun SavedSimulation.toSimulationResult() =
    SimulationsByRequestIdProjection.SimulationResult(
        birthdate = this.birthdate.toString(),
        amountOfMonths = this.amountOfMonths,
        loanAmount = this.loanAmount.toAmount(),
        totalAmount = this.totalAmount.toAmount(),
        monthlyPayment = this.monthlyPayment.toAmount(),
        totalInterest = this.totalInterest.toAmount(),
        createdAt = this.createdAt.epochSeconds,
    )

fun MonetaryAmount.toAmount() =
    SimulationsByRequestIdProjection.Amount(
        amount = this.toBigDecimal().movePointRight(2),
        currency = this.currency.currencyCode,
    )
