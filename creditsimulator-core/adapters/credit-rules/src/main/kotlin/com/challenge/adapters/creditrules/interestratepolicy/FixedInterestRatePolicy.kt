package com.challenge.adapters.creditrules.interestratepolicy

import com.challenge.creditsimulator.domain.getAgeFromBirthdate
import com.challenge.creditsimulator.domain.port.InterestRatePolicy
import com.challenge.creditsimulator.domain.simulation.Birthdate
import java.math.BigDecimal

class FixedInterestRatePolicy : InterestRatePolicy {
    override fun getAnnualRate(birthDate: Birthdate): BigDecimal {
        val age = getAgeFromBirthdate(birthDate)
        return when {
            age <= 25 -> BigDecimal("0.05")
            age in 26..40 -> BigDecimal("0.03")
            age in 41..60 -> BigDecimal("0.02")
            else -> BigDecimal("0.04")
        }
    }
}
