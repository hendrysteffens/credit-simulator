package com.challenge.adapters.http.simulation.request

import com.challenge.adapters.http.simulation.request.SimulateCreditInBatchRequest.SimulateCredit
import com.challenge.creditsimulator.application.simulation.command.SimulationBatchCommand
import com.challenge.creditsimulator.domain.withCurrency
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import javax.money.MonetaryAmount

@Serializable
data class SimulateCreditInBatchRequest(
    val batch: List<SimulateCredit>,
) {
    @Serializable
    data class SimulateCredit(
        val birthdate: LocalDate,
        val loanAmount: Amount,
        val amountOfMonths: Int,
    ) {
        @Serializable
        data class Amount(
            val value: Long,
            val currency: String,
        )
    }
}

private const val MONETARY_SCALE = 2

fun SimulateCredit.Amount.toMonetaryAmount(): MonetaryAmount {
    val value = BigDecimal.valueOf(this.value, MONETARY_SCALE)
    return value.withCurrency(this.currency)
}

fun SimulateCredit.toSimulationRequest() =
    SimulationBatchCommand.SimulationRequest(
        birthdate = this.birthdate,
        loanAmount = this.loanAmount.toMonetaryAmount(),
        amountOfMonths = this.amountOfMonths,
    )
