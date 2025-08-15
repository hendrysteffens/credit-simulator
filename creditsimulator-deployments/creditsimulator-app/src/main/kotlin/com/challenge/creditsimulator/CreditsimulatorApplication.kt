package com.challenge.creditsimulator

import com.challenge.adapters.creditrules.interestratepolicy.FakeMessagingProcessor
import com.challenge.adapters.creditrules.interestratepolicy.FixedInterestRatePolicy
import com.challenge.adapters.http.WebFluxConfiguration
import com.challenge.adapters.http.router
import com.challenge.adapters.http.simulation.SimulationHandler
import com.challenge.adapters.http.swaggerRoutes
import com.challenge.adapters.r2dbc.simulation.SimulationR2dbcRepository
import com.challenge.creditsimulator.application.simulation.command.SimulationBatchCommandHandler
import com.challenge.creditsimulator.application.simulation.command.SingleSimulationCommandHandler
import com.challenge.creditsimulator.application.simulation.query.SimulationsByRequestIdQueryHandler
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter
import java.util.Locale

@SpringBootApplication
@ConfigurationPropertiesScan
class CreditsimulatorApplication

fun main(args: Array<String>) {
    Locale.setDefault(Locale("pt", "BR"))
    runApplication<CreditsimulatorApplication>(*args) {
        addInitializers(
            ApplicationContextInitializer<GenericApplicationContext> { context ->
                beans().initialize(context)
            },
        )
    }
}

fun rootRouter() =
    coRouter {
        GET("/") { ok().buildAndAwait() }
    }

fun beans() =
    beans {
        // HTTP Handlers
        bean<SimulationHandler>()

        // Routers
        bean(::rootRouter)
        bean(::router)
        bean(::swaggerRoutes)
        bean<WebFluxConfiguration>()

        // Beans
        bean<SingleSimulationCommandHandler>()
        bean<SimulationBatchCommandHandler>()
        bean<SimulationsByRequestIdQueryHandler>()
        bean<FakeMessagingProcessor>()
        bean<SimulationR2dbcRepository>()
        bean<FixedInterestRatePolicy>()
    }
