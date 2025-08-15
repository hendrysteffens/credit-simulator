package com.challenge.creditsimulator.domain

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.days

object BirthdateExtensionsTestFixture {
    fun eighteenYearsOld(): Int = 18

    fun twoThousandYear(): Instant = Instant.parse("2000-01-01T10:00:00Z")

    fun birthdate() = twoThousandYear().toLocalDateTime(TimeZone.currentSystemDefault()).date

    fun birthdateAfterNow() =
        twoThousandYearPlusEighteenYears()
            .plus(2.days)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date

    fun twoThousandYearPlusEighteenYears() =
        twoThousandYear()
            .plus(
                period = DateTimePeriod(years = eighteenYearsOld()),
                TimeZone.currentSystemDefault(),
            )
}
