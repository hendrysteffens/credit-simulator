package com.challenge.adapters.r2dbc.simulation

import com.challenge.adapters.r2dbc.simulation.SimulationQueries.ADD_LIMIT
import com.challenge.adapters.r2dbc.simulation.SimulationQueries.ADD_OFFSET
import com.challenge.adapters.r2dbc.simulation.SimulationQueries.countSimulations
import com.challenge.adapters.r2dbc.simulation.SimulationQueries.insertSimulation
import com.challenge.adapters.r2dbc.simulation.SimulationQueries.selectSimulations
import com.challenge.adapters.r2dbc.simulation.SimulationQueries.whereRequestId
import com.challenge.creditsimulator.domain.port.SavedSimulation
import com.challenge.creditsimulator.domain.port.SimulationRepository
import com.challenge.creditsimulator.domain.simulation.Simulation
import com.challenge.creditsimulator.domain.toBigDecimal
import com.challenge.creditsimulator.domain.withCurrency
import io.github.oshai.kotlinlogging.KotlinLogging
import io.r2dbc.spi.Row
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinInstant
import kotlinx.datetime.toKotlinLocalDate
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.springframework.r2dbc.core.awaitOneOrNull
import org.springframework.r2dbc.core.flow
import java.math.BigDecimal
import java.util.UUID

class SimulationR2dbcRepository(
    private val db: DatabaseClient,
) : SimulationRepository {
    private companion object {
        val LOGGER = KotlinLogging.logger { }
    }

    override suspend fun save(simulation: Simulation) =
        withContext(Dispatchers.IO) {
            try {
                db
                    .sql(insertSimulation())
                    .bind("simulation_id", simulation.simulationId)
                    .bind("request_id", simulation.requestId)
                    .bind("status", simulation.status.toString())
                    .bind("birthdate", simulation.birthdate.toJavaLocalDate())
                    .bind("amount_of_months", simulation.amountOfMonths)
                    .bind("loan_amount", simulation.loanAmount.toBigDecimal())
                    .bind("currency", simulation.loanAmount.currency.currencyCode)
                    .bind("total_amount", simulation.totalAmount.toBigDecimal())
                    .bind("monthly_payment", simulation.monthlyPayment.toBigDecimal())
                    .bind("total_interest", simulation.totalInterest.toBigDecimal())
                    .bind("created_at", simulation.createdAt.toJavaInstant())
                    .await()
            } catch (ex: Exception) {
                LOGGER.error(ex) { ex.message }
            }
        }

    override suspend fun findSimulationPageByRequestId(
        requestId: UUID,
        offset: Int,
        limit: Int,
    ): ImmutableList<SavedSimulation> =
        withContext(Dispatchers.IO) {
            db
                .sql(
                    selectSimulations()
                        .where(whereRequestId())
                        .addClause(ADD_LIMIT)
                        .addClause(ADD_OFFSET),
                ).bind("request_id", requestId)
                .bind("limit", limit)
                .bind("offset", offset)
                .map { row, _ -> row.toSavedSimulation() }
                .flow()
                .toList()
                .toImmutableList()
        }

    private fun Row.toSavedSimulation() =
        SavedSimulation(
            requestId = get<UUID>("request_id"),
            birthdate = get<java.time.LocalDate>("birthdate").toKotlinLocalDate(),
            amountOfMonths = get<Int>("amount_of_months"),
            loanAmount = get<BigDecimal>("loan_amount").withCurrency(get<String>("currency")),
            totalAmount = get<BigDecimal>("total_amount").withCurrency(get<String>("currency")),
            monthlyPayment = get<BigDecimal>("monthly_payment").withCurrency(get<String>("currency")),
            totalInterest = get<BigDecimal>("total_interest").withCurrency(get<String>("currency")),
            createdAt = get<java.time.Instant>("created_at").toKotlinInstant(),
        )

    override suspend fun getTotalSimulationsByRequestId(requestId: UUID): Int =
        withContext(Dispatchers.IO) {
            db
                .sql(
                    countSimulations()
                        .where(
                            whereRequestId(),
                        ),
                ).bind("request_id", requestId)
                .map { row, _ -> row.getOrNull<Int>("total") }
                .awaitOneOrNull() ?: 0
        }
}
