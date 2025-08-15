package com.challenge.creditsimulator.domain.port

import com.challenge.creditsimulator.domain.simulation.Birthdate
import java.math.BigDecimal
import java.util.UUID
import javax.money.MonetaryAmount

interface SimulationMessageProcessor {
    suspend fun execute(message: SimulationMessage)
}

data class SimulationMessage(
    val requestId: UUID,
    val birthdate: Birthdate,
    val loanAmount: MonetaryAmount,
    val annualRate: BigDecimal,
    val months: Int,
)
