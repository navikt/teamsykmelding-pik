package no.nav.syfo.util

import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule
import tools.jackson.module.kotlin.readValue
import no.nav.syfo.etterlevelse.model.JuridiskVurderingResult
import org.apache.kafka.common.serialization.Deserializer

class JacksonKafkaDeserializer : Deserializer<JuridiskVurderingResult> {
    private val objectMapper =
        JsonMapper.builder()
            .addModule(kotlinModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
            .build()

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) {}

    override fun deserialize(topic: String, data: ByteArray): JuridiskVurderingResult {
        return objectMapper.readValue(data)
    }

    override fun close() {}
}
