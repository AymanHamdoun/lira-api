package lira.ahamdoun

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lira.ahamdoun.jobs.LiraRateJob
import lira.ahamdoun.plugins.configureRouting

fun main() {
    GlobalScope.launch {
        while(true) {
            LiraRateJob.getAndSaveJobData()
            Thread.sleep(5 * 60 * 1000)
        }
    }

    embeddedServer(Netty, port = 8080) {
        configureRouting();
    }.start(wait = true)
}
