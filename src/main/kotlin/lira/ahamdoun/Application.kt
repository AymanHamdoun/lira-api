package lira.ahamdoun

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import lira.ahamdoun.controller.LiraRatesController
import lira.ahamdoun.plugins.*

fun main() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/rates") {
                call.respondText {
                    val controller = LiraRatesController()
                    controller.getLiraRate()
                }
            }
        }
    }.start(wait = true)
}
