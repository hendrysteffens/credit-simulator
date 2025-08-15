package com.challenge.creditsimulator.domain.port

import com.challenge.creditsimulator.domain.simulation.Simulation
import kotlinx.collections.immutable.ImmutableList
import java.util.UUID

interface SimulationRepository {
    suspend fun save(simulation: Simulation)

    suspend fun findSimulationPageByRequestId(
        requestId: UUID,
        offset: Int,
        limit: Int,
    ): ImmutableList<SavedSimulation>

    suspend fun getTotalSimulationsByRequestId(requestId: UUID): Int
}
