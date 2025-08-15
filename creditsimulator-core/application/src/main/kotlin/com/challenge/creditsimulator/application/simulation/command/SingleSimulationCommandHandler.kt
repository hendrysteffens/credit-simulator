package com.challenge.creditsimulator.application.simulation.command

import com.challenge.creditsimulator.domain.port.InterestRatePolicy
import com.challenge.creditsimulator.domain.simulation.Simulation

class SingleSimulationCommandHandler(
    private val interestRatePolicy: InterestRatePolicy,
) {
    fun handle(command: SingleSimulationCommand): Simulation {
        val annualRate = interestRatePolicy.getAnnualRate(command.birthdate)
        val simulation =
            Simulation.create(
                birthdate = command.birthdate,
                loanAmount = command.loanAmount,
                annualRate = annualRate,
                months = command.amountOfMonths,
            )
        return simulation
    }
}
