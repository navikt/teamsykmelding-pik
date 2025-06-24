package no.nav.syfo

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import no.nav.syfo.plugins.configureConsumer
import no.nav.syfo.plugins.configureDependencyInjection
import no.nav.syfo.plugins.configureInternalRouting
import org.koin.ktor.ext.get

fun main(args: Array<String>) {
    EngineMain.main(args)
}


fun Application.module() {
    configureDependencyInjection()
    configureInternalRouting()
    configureConsumer(get())
}
