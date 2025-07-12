package no.nav.syfo

import io.kotest.core.spec.style.FunSpec
import io.ktor.http.HttpMethod
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import no.nav.syfo.plugins.configureInternalRouting
import org.amshove.kluent.shouldBeEqualTo

class SelftestSpec :
    FunSpec({
        context("Successfull liveness and readyness tests") {

                test("Returns ok on is_alive") {
                    testApplication {
                    application {
                        configureInternalRouting()
                    }
                    val response = client.get("/internal/is_alive")
                        response.status shouldBeEqualTo HttpStatusCode.OK
                        response.bodyAsText() shouldBeEqualTo "I'm alive! :)"
                    }
                }
                test("Returns ok in is_ready") {
                    testApplication {
                        application {
                        configureInternalRouting()
                        }
                        val response = client.get("/internal/is_ready")
                        response.status shouldBeEqualTo HttpStatusCode.OK
                        response.bodyAsText() shouldBeEqualTo "I'm ready! :)"
                    }
                }
            }
    })
