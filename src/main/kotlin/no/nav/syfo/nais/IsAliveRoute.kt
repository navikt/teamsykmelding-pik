package no.nav.syfo.nais

import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get

fun Routing.naisIsAliveRoute() {
    get("/internal/is_alive") {
        call.respondText("I'm alive! :)")
    }
}
