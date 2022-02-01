package no.nav.syfo.etterlevelse.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class JuridiskVurderingKafkaMessage(
    @JsonProperty("@id")
    val id: UUID,
    @JsonProperty("@event_name")
    val event_name: String,
    @JsonProperty("@versjon")
    val versjon: String,
    @JsonProperty("@kilde")
    val kilde: String,
    val versjonAvKode: String,
    val fodselsnummer: String,
    val sporing: Map<String, String>,
    val lovverk: String,
    val lovverksversjon: String,
    val paragraf: String,
    val ledd: Int?,
    val punktum: Int?,
    val bokstav: String?,
    val input: Map<String, Any>,
    val output: Map<String, Any>?,
    val utfall: Utfall
)

enum class Utfall {
    VILKAR_OPPFYLT,
    VILKAR_IKKE_OPPFYLT,
    VILKAR_UAVKLART,
    VILKAR_BEREGNET
}
