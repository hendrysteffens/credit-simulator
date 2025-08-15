package com.challenge.adapters.http

import com.challenge.adapters.http.simulation.request.SimulateCreditInBatchRequest
import com.challenge.creditsimulator.application.simulation.query.SimulationsByRequestIdProjection
import com.challenge.creditsimulator.domain.simulation.Simulation
import com.challenge.creditsimulator.domain.simulation.Status
import com.challenge.creditsimulator.domain.withCurrency
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import java.math.BigDecimal
import java.util.UUID
import javax.money.Monetary
import javax.money.MonetaryAmount

object RouterTestFixture {
    val currency = Monetary.getCurrency("BRL")

    fun validLoanAmount(amount: BigDecimal = BigDecimal("1000.00")): MonetaryAmount = amount.withCurrency(currency)

    fun validRequestBody(
        birthdate: String = "1990-01-01",
        amount: BigDecimal = BigDecimal("1000.00"),
        currency: String = "BRL",
        months: Int = 12,
    ): String =
        """
        {
            "birthdate": "$birthdate",
            "loanAmount": {
                "amount": $amount,
                "currency": "$currency"
            },
            "amountOfMonths": $months
        }
        """.trimIndent()

    fun createSampleSimulationWithValues(): Simulation =
        Simulation(
            simulationId = UUID.randomUUID(),
            requestId = UUID.randomUUID(),
            status = Status.COMPLETED,
            birthdate = LocalDate(1990, 1, 1),
            amountOfMonths = 12,
            loanAmount = validLoanAmount(),
            totalAmount =
                Monetary
                    .getDefaultAmountFactory()
                    .setCurrency(currency)
                    .setNumber(1016.28)
                    .create(),
            monthlyPayment =
                Monetary
                    .getDefaultAmountFactory()
                    .setCurrency(currency)
                    .setNumber(84.69)
                    .create(),
            totalInterest =
                Monetary
                    .getDefaultAmountFactory()
                    .setCurrency(currency)
                    .setNumber(16.28)
                    .create(),
            createdAt = Clock.System.now(),
            updatedAt = null,
        )

    fun validBatchSimulationRequest(): SimulateCreditInBatchRequest =
        SimulateCreditInBatchRequest(
            batch =
                listOf(
                    SimulateCreditInBatchRequest.SimulateCredit(
                        birthdate = LocalDate.parse("1985-05-15"),
                        loanAmount =
                            SimulateCreditInBatchRequest.SimulateCredit.Amount(
                                value = 1500000,
                                currency = "BRL",
                            ),
                        amountOfMonths = 24,
                    ),
                    SimulateCreditInBatchRequest.SimulateCredit(
                        birthdate = LocalDate.parse("1990-11-20"),
                        loanAmount =
                            SimulateCreditInBatchRequest.SimulateCredit.Amount(
                                value = 2500000,
                                currency = "BRL",
                            ),
                        amountOfMonths = 36,
                    ),
                ),
        )

    // Projeção para busca paginada
    fun createSampleProjection(requestId: UUID): SimulationsByRequestIdProjection =
        SimulationsByRequestIdProjection(
            simulations =
                persistentListOf(
                    SimulationsByRequestIdProjection.SimulationResult(
                        birthdate = "1985-05-15",
                        amountOfMonths = 24,
                        loanAmount =
                            SimulationsByRequestIdProjection.Amount(
                                amount = BigDecimal.valueOf(15000.00),
                                currency = "BRL",
                            ),
                        totalAmount =
                            SimulationsByRequestIdProjection.Amount(
                                amount = BigDecimal.valueOf(17412.00),
                                currency = "BRL",
                            ),
                        monthlyPayment =
                            SimulationsByRequestIdProjection.Amount(
                                amount = BigDecimal.valueOf(725.50),
                                currency = "BRL",
                            ),
                        totalInterest =
                            SimulationsByRequestIdProjection.Amount(
                                amount = BigDecimal.valueOf(2412.00),
                                currency = "BRL",
                            ),
                        createdAt = System.currentTimeMillis() / 1000,
                    ),
                ),
            totalPages = 1,
            totalItems = 1,
            offset = 0,
            limit = 5,
        )
}
