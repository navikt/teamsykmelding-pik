package no.nav.syfo.etterlevelse

import com.networknt.schema.Error
import com.networknt.schema.InputFormat
import com.networknt.schema.Schema
import com.networknt.schema.SchemaLocation
import com.networknt.schema.SchemaRegistry
import com.networknt.schema.SpecificationVersion
import org.junit.jupiter.api.Assertions.assertEquals

internal object SchemaAssertions {
    private val schema: Schema by lazy {
        SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7) { builder ->
            builder.schemaLoader { it.fetchRemoteResources() }
        }.getSchema(
            SchemaLocation.of(
                "https://raw.githubusercontent.com/navikt/helse/main/subsumsjon/json-schema-1.0.0.json"
            )
        )
    }

    internal fun assertSchema(melding: String) {
        assertEquals(emptyList<Error>(), schema.validate(melding, InputFormat.JSON))
    }
}
