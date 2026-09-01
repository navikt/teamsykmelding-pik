package no.nav.syfo.etterlevelse

import com.networknt.schema.InputFormat
import com.networknt.schema.SchemaLocation
import io.kotest.matchers.equals.shouldBeEqual
import com.networknt.schema.SchemaRegistry
import com.networknt.schema.SpecificationVersion
import kotlin.getValue

internal object SchemaAssertions {
    private val schema by lazy {
        val registry =
            SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_7) { builder ->
                builder.schemaLoader { it.fetchRemoteResources() }
            }
        registry.getSchema(
            SchemaLocation.of(
                "https://raw.githubusercontent.com/navikt/helse/main/subsumsjon/json-schema-1.0.0.json"
            )
        )
    }


    internal fun assertSchema(melding: String) {
        schema.validate(melding, InputFormat.JSON) shouldBeEqual emptyList<Error>()
    }}
