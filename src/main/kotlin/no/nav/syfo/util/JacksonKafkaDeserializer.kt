package no.nav.syfo.util

import no.nav.syfo.etterlevelse.model.JuridiskVurderingResult
import org.apache.kafka.common.serialization.Deserializer
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonMapperBuilder
import tools.jackson.module.kotlin.readValue

class JacksonKafkaDeserializer : Deserializer<JuridiskVurderingResult> {
    private val jsonMapper: JsonMapper =
        jacksonMapperBuilder()
            .enable(
                tools.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
            )
            .build()

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) {}

    override fun deserialize(topic: String, data: ByteArray): JuridiskVurderingResult {
        return jsonMapper.readValue(data)
    }

    override fun close() {}
}
