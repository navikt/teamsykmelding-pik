package no.nav.syfo.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import no.nav.syfo.etterlevelse.EtterlevelseService


fun Application.configureConsumer(etterlevelseService: EtterlevelseService) {
    val etterlevelseJob = launch(Dispatchers.IO) { etterlevelseService.startConsumer() }
    environment.monitor.subscribe(ApplicationStopping) {
        etterlevelseJob.cancel()
    }
}
