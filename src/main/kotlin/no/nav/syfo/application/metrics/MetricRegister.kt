package no.nav.syfo.application.metrics

import io.prometheus.client.Counter
import io.prometheus.client.Histogram

const val METRICS_NS = "teamsykmelding_pik"

val HTTP_HISTOGRAM: Histogram =
    Histogram.Builder()
        .labelNames("path")
        .name("requests_duration_seconds")
        .help("http requests durations for incoming requests in seconds")
        .register()

val PRODUCED_MESSAGE_COUNTER: Counter =
    Counter.build()
        .name("produced_msg_counter")
        .namespace(METRICS_NS)
        .help("Antall produserte meldinger")
        .register()
