package no.nav.syfo.etterlevelse.model

import no.nav.tsm.regulus.regula.juridisk.JuridiskUtfall
import java.time.OffsetDateTime
import java.util.UUID

data class JuridiskVurderingKafkaMessage(
    val id: UUID,
    val tidsstempel: OffsetDateTime,
    val eventName: String,
    val versjon: String,
    val kilde: String,
    val versjonAvKode: String,
    val fodselsnummer: String,
    val sporing: Map<String, List<String>>,
    val lovverk: String,
    val lovverksversjon: String,
    val paragraf: String,
    val ledd: Int?,
    val punktum: Int?,
    val bokstav: String?,
    val input: Map<String, Any>,
    val output: Map<String, Any>?,
    val utfall: JuridiskUtfall
)
