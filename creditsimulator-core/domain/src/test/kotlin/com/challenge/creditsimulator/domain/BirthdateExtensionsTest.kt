package com.challenge.creditsimulator.domain

import com.challenge.creditsimulator.domain.BirthdateExtensionsTestFixture.birthdate
import com.challenge.creditsimulator.domain.BirthdateExtensionsTestFixture.birthdateAfterNow
import com.challenge.creditsimulator.domain.BirthdateExtensionsTestFixture.eighteenYearsOld
import com.challenge.creditsimulator.domain.BirthdateExtensionsTestFixture.twoThousandYearPlusEighteenYears
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import kotlinx.datetime.Clock
import kotlinx.datetime.Clock.System.now
import java.math.BigDecimal.ZERO

class BirthdateExtensionsTest :
    DescribeSpec({

        beforeSpec {
            mockkObject(Clock.System)
            every { now() } returns twoThousandYearPlusEighteenYears()
        }

        describe("Birthdate extensions") {
            context("Success cases") {
                it("Should return 18 when today is 2018 and birthdate is 2000") {
                    getAgeFromBirthdate(birthdate()) shouldBe eighteenYearsOld()
                }
            }
            context("Fail cases") {
                it("Should return 0 when birthdate is after now()") {
                    getAgeFromBirthdate(birthdateAfterNow()) shouldBe ZERO.toInt()
                }
            }
        }
    })
