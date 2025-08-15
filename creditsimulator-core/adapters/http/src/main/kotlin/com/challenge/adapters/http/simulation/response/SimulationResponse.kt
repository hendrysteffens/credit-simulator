package com.challenge.adapters.http.simulation.response

import com.challenge.adapters.http.simulation.response.SimulationResponse.Amount
import com.challenge.creditsimulator.domain.simulation.Simulation
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import javax.money.MonetaryAmount

@Serializable
data class SimulationResponse(
    val birthdate: LocalDate? = null,
    val amountOfMonths: Int,
    val loanAmount: Amount,
    val totalAmount: Amount,
    val monthlyPayment: Amount,
    val totalInterest: Amount,
) {
    @Serializable
    data class Amount(
        val value: Long,
        val currency: String = "BRL",
    )
}

private const val MONETARY_SCALE = 2

fun Simulation.toResponse(): SimulationResponse =
    SimulationResponse(
        birthdate = this.birthdate,
        amountOfMonths = this.amountOfMonths,
        loanAmount = this.loanAmount.toAmountResponse(),
        totalAmount = this.totalAmount.toAmountResponse(),
        monthlyPayment = this.monthlyPayment.toAmountResponse(),
        totalInterest = this.totalInterest.toAmountResponse(),
    )

fun MonetaryAmount.toAmountResponse(): Amount {
    val unscaledValue =
        this.number
            .numberValueExact(BigDecimal::class.java)
            .movePointRight(MONETARY_SCALE)
            .longValueExact()
    return Amount(value = unscaledValue, this.currency.currencyCode)
}
