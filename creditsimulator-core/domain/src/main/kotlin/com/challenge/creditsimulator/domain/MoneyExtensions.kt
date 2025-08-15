package com.challenge.creditsimulator.domain

import java.math.BigDecimal
import javax.money.CurrencyUnit
import javax.money.Monetary
import javax.money.MonetaryAmount

infix fun <T : Number> T.withCurrency(currency: String): MonetaryAmount =
    Monetary
        .getDefaultAmountFactory()
        .setNumber(this)
        .setCurrency(currency)
        .create()

infix fun <T : Number> T.withCurrency(currency: CurrencyUnit): MonetaryAmount =
    Monetary
        .getDefaultAmountFactory()
        .setNumber(this)
        .setCurrency(currency)
        .create()

fun MonetaryAmount.toBigDecimal(): BigDecimal = this.number.numberValueExact(BigDecimal::class.java)
