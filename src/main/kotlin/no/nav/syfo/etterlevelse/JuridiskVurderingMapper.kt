package no.nav.syfo.etterlevelse

import no.nav.syfo.etterlevelse.model.JuridiskVurderingKafkaMessage
import no.nav.syfo.etterlevelse.model.Utfall
import no.nav.syfo.model.juridisk.JuridiskUtfall
import no.nav.syfo.model.juridisk.JuridiskVurdering
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun JuridiskVurdering.tilJuridiskVurderingKafkaMessage(tidsstempel: LocalDateTime = LocalDateTime.now()): JuridiskVurderingKafkaMessage {
    return JuridiskVurderingKafkaMessage(
        id = UUID.fromString(id),
        tidsstempel = tidsstempel,
        eventName = eventName,
        versjon = version,
        kilde = kilde,
        versjonAvKode = versjonAvKode,
        fodselsnummer = fodselsnummer,
        sporing = sporing.mapValues { listOf(it.value) },
        lovverk = juridiskHenvisning.lovverk.kortnavn.lowercase(),
        lovverksversjon = juridiskHenvisning.lovverk.lovverksversjon.format(formatter),
        paragraf = juridiskHenvisning.paragraf,
        ledd = juridiskHenvisning.ledd,
        punktum = juridiskHenvisning.punktum,
        bokstav = juridiskHenvisning.bokstav,
        input = input,
        output = null,
        utfall = utfall.tilUtfall()
    )
}

fun JuridiskUtfall.tilUtfall(): Utfall {
    return when (this) {
        JuridiskUtfall.VILKAR_OPPFYLT -> Utfall.VILKAR_OPPFYLT
        JuridiskUtfall.VILKAR_BEREGNET -> Utfall.VILKAR_BEREGNET
        JuridiskUtfall.VILKAR_IKKE_OPPFYLT -> Utfall.VILKAR_IKKE_OPPFYLT
        JuridiskUtfall.VILKAR_UAVKLART -> Utfall.VILKAR_UAVKLART
    }
}
