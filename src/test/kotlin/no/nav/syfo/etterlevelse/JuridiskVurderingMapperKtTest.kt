package no.nav.syfo.etterlevelse

import io.kotest.core.spec.style.FunSpec
import no.nav.syfo.etterlevelse.model.JuridiskVurderingResult
import no.nav.syfo.etterlevelse.model.Utfall
import no.nav.syfo.model.juridisk.JuridiskHenvisning
import no.nav.syfo.model.juridisk.JuridiskUtfall
import no.nav.syfo.model.juridisk.JuridiskVurdering
import no.nav.syfo.model.juridisk.Lovverk
import org.amshove.kluent.shouldBeEqualTo
import java.time.LocalDateTime
import java.util.UUID

class JuridiskVurderingMapperKtTest : FunSpec({
    context("JuridiskVurderingMapper") {
        test("tilJuridiskVurderingKafkaMessage mapper korrekt") {
            val id = UUID.randomUUID()
            val sykmeldingId = UUID.randomUUID().toString()
            val juridiskVurderingResult = JuridiskVurderingResult(
                listOf(
                    JuridiskVurdering(
                        id = id.toString(),
                        eventName = "subsumsjon",
                        version = "1.0.0",
                        kilde = "syfosmregler",
                        versjonAvKode = "imagenavn",
                        fodselsnummer = "12345678910",
                        juridiskHenvisning = JuridiskHenvisning(
                            lovverk = Lovverk.FOLKETRYGDLOVEN,
                            paragraf = "§8-1",
                            ledd = 1,
                            punktum = 1,
                            bokstav = "a"
                        ),
                        sporing = mapOf("sykmeldingId" to sykmeldingId),
                        input = mapOf("input" to "verdi"),
                        utfall = JuridiskUtfall.VILKAR_OPPFYLT
                    )
                )
            )

            val tidsstempel = LocalDateTime.now()
            val juridiskVurderingKafkaMessage = juridiskVurderingResult.juridiskeVurderinger.first().tilJuridiskVurderingKafkaMessage(tidsstempel)

            juridiskVurderingKafkaMessage.id shouldBeEqualTo id
            juridiskVurderingKafkaMessage.tidsstempel shouldBeEqualTo tidsstempel
            juridiskVurderingKafkaMessage.eventName shouldBeEqualTo "subsumsjon"
            juridiskVurderingKafkaMessage.versjon shouldBeEqualTo "1.0.0"
            juridiskVurderingKafkaMessage.kilde shouldBeEqualTo "syfosmregler"
            juridiskVurderingKafkaMessage.versjonAvKode shouldBeEqualTo "imagenavn"
            juridiskVurderingKafkaMessage.fodselsnummer shouldBeEqualTo "12345678910"
            juridiskVurderingKafkaMessage.sporing shouldBeEqualTo mapOf("sykmeldingId" to listOf(sykmeldingId))
            juridiskVurderingKafkaMessage.lovverk shouldBeEqualTo "folketrygdloven"
            juridiskVurderingKafkaMessage.lovverksversjon shouldBeEqualTo "2022-01-01"
            juridiskVurderingKafkaMessage.paragraf shouldBeEqualTo "§8-1"
            juridiskVurderingKafkaMessage.ledd shouldBeEqualTo 1
            juridiskVurderingKafkaMessage.punktum shouldBeEqualTo 1
            juridiskVurderingKafkaMessage.bokstav shouldBeEqualTo "a"
            juridiskVurderingKafkaMessage.input shouldBeEqualTo mapOf("input" to "verdi")
            juridiskVurderingKafkaMessage.output shouldBeEqualTo null
            juridiskVurderingKafkaMessage.utfall shouldBeEqualTo Utfall.VILKAR_OPPFYLT
        }
    }
})
