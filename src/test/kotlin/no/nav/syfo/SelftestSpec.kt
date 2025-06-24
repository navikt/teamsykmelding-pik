package no.nav.syfo

import io.kotest.core.spec.style.FunSpec
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import no.nav.syfo.plugins.configureInternalRouting
import org.amshove.kluent.shouldBeEqualTo

class SelftestSpec :
    FunSpec({
        context("Successfull liveness and readyness tests") {
            with(TestApplicationEngine()) {
                start()
                application.configureInternalRouting()

                test("Returns ok on is_alive") {
                    with(handleRequest(HttpMethod.Get, "/internal/is_alive")) {
                        response.status() shouldBeEqualTo HttpStatusCode.OK
                        response.content shouldBeEqualTo "I'm alive! :)"
                    }
                }
                test("Returns ok in is_ready") {
                    with(handleRequest(HttpMethod.Get, "/internal/is_ready")) {
                        response.status() shouldBeEqualTo HttpStatusCode.OK
                        response.content shouldBeEqualTo "I'm ready! :)"
                    }
                }
            }
        }
    })
