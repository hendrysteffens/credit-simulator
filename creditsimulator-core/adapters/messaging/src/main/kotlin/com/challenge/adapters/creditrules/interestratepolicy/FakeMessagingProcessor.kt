package com.challenge.adapters.creditrules.interestratepolicy

import com.challenge.creditsimulator.domain.port.SimulationMessage
import com.challenge.creditsimulator.domain.port.SimulationMessageProcessor
import com.challenge.creditsimulator.domain.port.SimulationRepository
import com.challenge.creditsimulator.domain.simulation.Simulation

class FakeMessagingProcessor(
    private val simulationRepository: SimulationRepository,
) : SimulationMessageProcessor {
    override suspend fun execute(message: SimulationMessage) {
        Simulation
            .create(
                requestId = message.requestId,
                birthdate = message.birthdate,
                loanAmount = message.loanAmount,
                annualRate = message.annualRate,
                months = message.months,
            ).let {
                simulationRepository.save(it)
            }
    }
}
