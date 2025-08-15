package com.challenge.creditsimulator.domain.port

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import java.util.UUID
import javax.money.MonetaryAmount

data class SavedSimulation(
    val requestId: UUID,
    val birthdate: LocalDate,
    val amountOfMonths: Int,
    val loanAmount: MonetaryAmount,
    val totalAmount: MonetaryAmount,
    val monthlyPayment: MonetaryAmount,
    val totalInterest: MonetaryAmount,
    val createdAt: Instant,
)
