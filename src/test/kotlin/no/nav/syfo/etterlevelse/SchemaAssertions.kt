package no.nav.syfo.etterlevelse

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion.VersionFlag.V7
import com.networknt.schema.ValidationMessage
import org.junit.jupiter.api.Assertions.assertEquals
import java.net.URI

internal object SchemaAssertions {
    private val objectMapper = jacksonObjectMapper()
    private val schema by lazy {
        JsonSchemaFactory
            .getInstance(V7)
            .getSchema(URI("https://raw.githubusercontent.com/navikt/helse/main/subsumsjon/json-schema-1.0.0.json"))
    }

    private fun assertSchema(melding: JsonNode) {
        assertEquals(emptySet<ValidationMessage>(), schema.validate(melding))
    }

    internal fun assertSchema(melding: String) = assertSchema(objectMapper.readTree(melding))
}
