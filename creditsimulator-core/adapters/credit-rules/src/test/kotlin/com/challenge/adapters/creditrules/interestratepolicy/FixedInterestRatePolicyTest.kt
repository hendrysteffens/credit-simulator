package com.challenge.adapters.creditrules.interestratepolicy

import com.challenge.creditsimulator.domain.createBirthdateForAge
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.minus
import java.math.BigDecimal

class FixedInterestRatePolicyTest :
    DescribeSpec({
        val policy = FixedInterestRatePolicy()

        describe("FixedInterestRatePolicy") {
            context("getAnnualRate") {
                it("should return 5% for age <= 25") {
                    policy.getAnnualRate(createBirthdateForAge(0)) shouldBe BigDecimal("0.05")
                    policy.getAnnualRate(createBirthdateForAge(18)) shouldBe BigDecimal("0.05")
                    policy.getAnnualRate(createBirthdateForAge(25)) shouldBe BigDecimal("0.05")
                }

                it("should return 3% for age between 26 and 40") {
                    policy.getAnnualRate(createBirthdateForAge(26)) shouldBe BigDecimal("0.03")
                    policy.getAnnualRate(createBirthdateForAge(30)) shouldBe BigDecimal("0.03")
                    policy.getAnnualRate(createBirthdateForAge(40)) shouldBe BigDecimal("0.03")
                }

                it("should return 2% for age between 41 and 60") {
                    policy.getAnnualRate(createBirthdateForAge(41)) shouldBe BigDecimal("0.02")
                    policy.getAnnualRate(createBirthdateForAge(50)) shouldBe BigDecimal("0.02")
                    policy.getAnnualRate(createBirthdateForAge(60)) shouldBe BigDecimal("0.02")
                }

                it("should return 4% for age > 60") {
                    policy.getAnnualRate(createBirthdateForAge(61)) shouldBe BigDecimal("0.04")
                    policy.getAnnualRate(createBirthdateForAge(70)) shouldBe BigDecimal("0.04")
                    policy.getAnnualRate(createBirthdateForAge(100)) shouldBe BigDecimal("0.04")
                }

                it("should handle exact boundary dates correctly") {
                    // 25 anos HOJE (ainda na faixa)
                    val exactly25 = createBirthdateForAge(25)
                    policy.getAnnualRate(exactly25) shouldBe BigDecimal("0.05")

                    // 26 anos AMANHÃ (ainda 25 hoje)
                    val tomorrow26 =
                        createBirthdateForAge(25)
                            .minus(DatePeriod(days = 1))
                    policy.getAnnualRate(tomorrow26) shouldBe BigDecimal("0.05")

                    // 26 anos HOJE
                    val exactly26 = createBirthdateForAge(26)
                    policy.getAnnualRate(exactly26) shouldBe BigDecimal("0.03")
                }
            }
        }
    })
