package com.challenge.creditsimulator.domain

import com.challenge.creditsimulator.domain.simulation.Birthdate
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil

fun getAgeFromBirthdate(birthdate: Birthdate): Int =
    birthdate
        .atStartOfDayIn(
            TimeZone.currentSystemDefault(),
        ).yearsUntil(
            Clock.System.now(),
            TimeZone.currentSystemDefault(),
        )

fun createBirthdateForAge(age: Int): LocalDate =
    Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .minus(period = DatePeriod(years = age))
