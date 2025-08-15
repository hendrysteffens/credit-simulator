package com.challenge.adapters.r2dbc.simulation

import io.r2dbc.spi.Row
import kotlin.jvm.java
import kotlin.let
import kotlin.text.trimEnd

@Suppress("EXTENSION_SHADOWED_BY_MEMBER")
inline fun <reified T> Row.get(identifier: String): T = this.get(identifier, T::class.java)!!

inline fun <reified T> Row.getOrNull(identifier: String): T? = this.get(identifier, T::class.java)

fun String.where(sql: String?) = sql?.let { "${this.trimEnd()} $sql" } ?: this

fun String.addClause(sql: String) = sql.let { "${this.trimEnd()} $sql" } ?: this

fun String.orderBy(sql: String?) = sql?.let { "${this.trimEnd()} $sql" } ?: this
