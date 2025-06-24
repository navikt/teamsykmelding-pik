package no.nav.syfo.plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import io.prometheus.client.hotspot.DefaultExports
import no.nav.syfo.nais.naisIsAliveRoute
import no.nav.syfo.nais.naisIsReadyRoute
import no.nav.syfo.nais.naisPrometheusRoute
fun Application.configureInternalRouting(
) {
    routing {
        naisIsAliveRoute()
        naisIsReadyRoute()
        naisPrometheusRoute()
    }

    DefaultExports.initialize()
}
