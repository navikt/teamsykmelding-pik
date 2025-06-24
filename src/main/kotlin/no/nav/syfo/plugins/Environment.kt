package no.nav.syfo.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.host
import java.util.Properties

class Environment(
    val kafkaConfig: Properties,
    val hostname: String,
    val internPikTopic: String = "teamsykmelding.paragraf-i-kode",
    val etterlevelseTopic: String = "flex.omrade-helse-etterlevelse",
    val cluster: String = getEnvVar("NAIS_CLUSTER_NAME"),
)

fun Application.createEnvironment(): Environment {
    return Environment(
        kafkaConfig = Properties().apply {
            environment.config.config("ktor.kafka.config").toMap().forEach {
                this[it.key] = it.value
            }
        },
        hostname = environment.config.host,
    )
}

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName)
        ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")
