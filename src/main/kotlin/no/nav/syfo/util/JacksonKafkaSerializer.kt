package no.nav.syfo.util

import tools.jackson.databind.cfg.DateTimeFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.kotlinModule
import org.apache.kafka.common.serialization.Serializer

class JacksonKafkaSerializer : Serializer<Any> {
    private lateinit var objectMapper: JsonMapper

    override fun configure(configs: MutableMap<String, *>, isKey: Boolean) {
        objectMapper = JsonMapper.builder()
            .addModule(kotlinModule())
            .configure(
                DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS,
                configs[SERIALIZE_AS_TIMESTAMP] == false
            )
            .build()
    }

    override fun serialize(topic: String?, data: Any?): ByteArray =
        objectMapper.writeValueAsBytes(data)

    override fun close() {}

    companion object {
        const val SERIALIZE_AS_TIMESTAMP = "no.nav.serialize.as.timestamp"
    }
}
