package no.nav.syfo

data class Environment(
    val applicationPort: Int = getEnvVar("APPLICATION_PORT", "8080").toInt(),
    val applicationName: String = getEnvVar("NAIS_APP_NAME", "teamsykmelding-pik"),
    val cluster: String = getEnvVar("NAIS_CLUSTER_NAME"),
    val internPikTopic: String = "teamsykmelding.paragraf-i-kode",
    val etterlevelseTopic: String = "flex.omrade-helse-etterlevelse" // skal byttes ut med reell topic når denne er klar
)

fun getEnvVar(varName: String, defaultValue: String? = null) =
    System.getenv(varName) ?: defaultValue ?: throw RuntimeException("Missing required variable \"$varName\"")
