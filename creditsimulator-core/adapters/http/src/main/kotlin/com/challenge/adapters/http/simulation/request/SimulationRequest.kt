package com.challenge.adapters.http.simulation.request

import com.challenge.creditsimulator.domain.withCurrency
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import javax.money.MonetaryAmount

@Serializable
data class SimulationRequest(
    val birthdate: LocalDate,
    val loanAmount: Amount,
    val amountOfMonths: Int,
)

@Serializable
data class Amount(
    val value: Long,
    val currency: String,
)

private const val MONETARY_SCALE = 2

fun Amount.toMonetaryAmount(): MonetaryAmount {
    val value = BigDecimal.valueOf(this.value, MONETARY_SCALE)
    return value.withCurrency(this.currency)
}
