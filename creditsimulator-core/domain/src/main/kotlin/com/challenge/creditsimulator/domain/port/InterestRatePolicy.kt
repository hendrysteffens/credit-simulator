package com.challenge.creditsimulator.domain.port

import com.challenge.creditsimulator.domain.simulation.Birthdate
import java.math.BigDecimal

interface InterestRatePolicy {
    fun getAnnualRate(birthDate: Birthdate): BigDecimal
}
