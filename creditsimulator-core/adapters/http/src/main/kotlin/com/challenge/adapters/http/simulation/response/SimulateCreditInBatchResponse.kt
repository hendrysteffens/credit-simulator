package com.challenge.adapters.http.simulation.response

import kotlinx.serialization.Serializable

@Serializable
data class SimulateCreditInBatchResponse(
    val requestId: String,
)
