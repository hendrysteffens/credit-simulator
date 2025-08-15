package com.challenge.adapters.http

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.web.reactive.config.WebFluxConfigurer

class WebFluxConfiguration : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        super.configureHttpMessageCodecs(configurer)

        configurer.defaultCodecs().kotlinSerializationJsonDecoder(
            KotlinSerializationJsonDecoder(
                @OptIn(ExperimentalSerializationApi::class) (
                    Json {
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                ),
            ),
        )
    }
}
