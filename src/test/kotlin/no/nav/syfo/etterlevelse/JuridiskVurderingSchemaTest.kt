package no.nav.syfo.etterlevelse

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.core.spec.style.FunSpec
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.UUID
import no.nav.syfo.etterlevelse.model.JuridiskVurderingResult
import no.nav.syfo.model.juridisk.JuridiskHenvisning
import no.nav.syfo.model.juridisk.JuridiskUtfall
import no.nav.syfo.model.juridisk.JuridiskVurdering
import no.nav.syfo.model.juridisk.Lovverk

class JuridiskVurderingSchemaTest :
    FunSpec({
        val objectMapper =
            ObjectMapper()
                .registerModule(JavaTimeModule())
                .registerKotlinModule()
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

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
                val kafkaMessage = objectMapper.writeValueAsString(juridiskVurderingKafkaMessage)

                SchemaAssertions.assertSchema(kafkaMessage)
            }
        }
    })
