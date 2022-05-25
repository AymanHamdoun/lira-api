package lira.ahamdoun.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import lira.ahamdoun.controller.LiraRatesController
import lira.ahamdoun.controller.UserController
import java.io.File

fun Application.configureRouting() {

    // Starting point for a Ktor app:
    routing {
        get("/") {
            call.respondFile(File("index.html"))
        }

        get("/rates") {
            call.respondText {
                val params = call.request.queryParameters
                val controller = LiraRatesController(params)
                controller.getLiraRate()
            }
        }

        post("/users/store") {
            call.respondText {
                val params = call.receiveParameters()
                val controller = UserController(params)
                controller.register()
            }
        }
    }
}
