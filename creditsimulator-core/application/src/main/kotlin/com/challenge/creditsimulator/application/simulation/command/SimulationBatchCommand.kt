package com.challenge.creditsimulator.application.simulation.command

import com.challenge.creditsimulator.application.simulation.command.SimulationBatchCommand.SimulationRequest
import com.challenge.creditsimulator.domain.port.SimulationMessage
import kotlinx.datetime.LocalDate
import java.math.BigDecimal
import java.util.UUID
import javax.money.MonetaryAmount

data class SimulationBatchCommand(
    val simulations: List<SimulationRequest>,
) {
    data class SimulationRequest(
        val birthdate: LocalDate,
        val amountOfMonths: Int,
        val loanAmount: MonetaryAmount,
    )
}

fun SimulationRequest.toMessage(
    requestId: UUID,
    annualRate: BigDecimal,
): SimulationMessage =
    SimulationMessage(
        requestId = requestId,
        birthdate = this.birthdate,
        loanAmount = this.loanAmount,
        annualRate = annualRate,
        months = amountOfMonths,
    )
