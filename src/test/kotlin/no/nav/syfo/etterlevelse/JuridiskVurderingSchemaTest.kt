package no.nav.syfo.etterlevelse

import io.kotest.core.spec.style.FunSpec
import no.nav.syfo.etterlevelse.model.JuridiskHenvisning
import no.nav.syfo.etterlevelse.model.JuridiskUtfall
import no.nav.syfo.etterlevelse.model.JuridiskVurdering
import no.nav.syfo.etterlevelse.model.JuridiskVurderingResult
import no.nav.syfo.etterlevelse.model.Lovverk
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.jacksonMapperBuilder
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID

class JuridiskVurderingSchemaTest :
    FunSpec({
        val jsonMapper: JsonMapper = jacksonMapperBuilder().build()

        context("JuridiskVurderingMapper") {
            test("Skjema er riktig") {
                val id = UUID.randomUUID()
                val sykmeldingId = UUID.randomUUID().toString()
                val juridiskVurderingResult =
                    JuridiskVurderingResult(
                        listOf(
                            JuridiskVurdering(
                                id = id.toString(),
                                eventName = "subsumsjon",
                                version = "1.0.0",
                                kilde = "syfosmregler",
                                versjonAvKode = "imagenavn",
                                fodselsnummer = "12345678910",
                                juridiskHenvisning =
                                    JuridiskHenvisning(
                                        lovverk = Lovverk.FOLKETRYGDLOVEN,
                                        paragraf = "8-1",
                                        ledd = 1,
                                        punktum = 1,
                                        bokstav = "a"
                                    ),
                                sporing = mapOf("sykmelding" to sykmeldingId),
                                input = mapOf("input" to "verdi"),
                                utfall = JuridiskUtfall.VILKAR_OPPFYLT,
                                tidsstempel = ZonedDateTime.now(ZoneOffset.UTC)
                            )
                        )
                    )

                val tidsstempel = OffsetDateTime.now()
                val juridiskVurderingKafkaMessage =
                    juridiskVurderingResult.juridiskeVurderinger
                        .first()
                        .tilJuridiskVurderingKafkaMessage(tidsstempel)
                val kafkaMessage = jsonMapper.writeValueAsString(juridiskVurderingKafkaMessage)

                SchemaAssertions.assertSchema(kafkaMessage)
            }
        }
    })
