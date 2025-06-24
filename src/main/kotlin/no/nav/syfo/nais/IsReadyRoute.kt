package no.nav.syfo.nais

import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.naisIsReadyRoute(
) {
    get("/internal/is_ready") {
        call.respondText("I'm ready! :)")
    }
}
