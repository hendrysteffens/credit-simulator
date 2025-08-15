package com.challenge.creditsimulator.application.simulation.command

import kotlinx.datetime.LocalDate
import javax.money.MonetaryAmount

data class SingleSimulationCommand(
    val birthdate: LocalDate,
    val amountOfMonths: Int,
    val loanAmount: MonetaryAmount,
)
