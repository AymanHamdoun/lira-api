package lira.ahamdoun

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lira.ahamdoun.controller.LiraRatesController
import lira.ahamdoun.jobs.LiraRateJob

fun main() {
    GlobalScope.launch {
        while(true) {
            println("Job Triggerred")
            LiraRateJob.getAndSaveJobData()
            Thread.sleep(5 * 60 * 1000)
        }
    }

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
