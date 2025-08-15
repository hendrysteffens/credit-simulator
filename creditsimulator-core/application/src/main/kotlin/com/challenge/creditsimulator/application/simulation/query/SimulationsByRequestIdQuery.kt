package com.challenge.creditsimulator.application.simulation.query

import java.util.UUID

data class SimulationsByRequestIdQuery(
    val requestId: UUID,
    val offset: Int,
    val limit: Int,
)
