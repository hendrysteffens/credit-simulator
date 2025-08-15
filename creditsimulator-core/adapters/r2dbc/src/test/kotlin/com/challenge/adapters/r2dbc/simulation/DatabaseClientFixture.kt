package com.challenge.adapters.r2dbc.simulation

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import org.springframework.r2dbc.core.DatabaseClient

object DatabaseClientFixture {
    fun databaseClient(): DatabaseClient {
        val factory =
            PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration
                    .builder()
                    .host("localhost")
                    .port(5432)
                    .schema("public")
                    .username("admin")
                    .password("admin")
                    .database("credit")
                    .build(),
            )

        return DatabaseClient.builder().connectionFactory(factory).build()
    }
}
