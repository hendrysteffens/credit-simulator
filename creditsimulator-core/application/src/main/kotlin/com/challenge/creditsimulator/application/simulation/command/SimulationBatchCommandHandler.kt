package com.challenge.creditsimulator.application.simulation.command

import com.challenge.creditsimulator.domain.port.InterestRatePolicy
import com.challenge.creditsimulator.domain.port.SimulationMessageProcessor
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.UUID

class SimulationBatchCommandHandler(
    private val interestRatePolicy: InterestRatePolicy,
    private val processor: SimulationMessageProcessor,
) {
    suspend fun handle(command: SimulationBatchCommand): UUID {
        val requestId = UUID.randomUUID()
        supervisorScope {
            // TODO: Criar uma tabela de request para linkar as simulacoes
            //  e notificar quando terminar de executar todas em assinc
            command.simulations.forEach {
                launch {
                    val annualRate = interestRatePolicy.getAnnualRate(it.birthdate)
                    processor.execute(
                        message = it.toMessage(requestId, annualRate),
                    )
                }
            }
        }
        return requestId
    }
}
