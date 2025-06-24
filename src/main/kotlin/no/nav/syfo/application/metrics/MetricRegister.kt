package no.nav.syfo.application.metrics

import io.prometheus.client.Counter

const val METRICS_NS = "teamsykmelding_pik"

val PRODUCED_MESSAGE_COUNTER: Counter =
    Counter.build()
        .name("produced_msg_counter")
        .namespace(METRICS_NS)
        .help("Antall produserte meldinger")
        .register()
