package no.nav.syfo.etterlevelse.model

import no.nav.syfo.model.juridisk.JuridiskVurdering

data class JuridiskVurderingResult(
    val juridiskeVurderinger: List<JuridiskVurdering>
)
