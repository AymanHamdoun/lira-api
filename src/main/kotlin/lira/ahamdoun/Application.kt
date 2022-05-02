package lira.ahamdoun

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lira.ahamdoun.jobs.LiraRateJob
import lira.ahamdoun.plugins.configureRouting
import lira.ahamdoun.repositories.UserRepository
import lira.ahamdoun.utility.Log

fun main() {
    val repo = UserRepository()
    val user1 = repo.getByID(1)
    val user2 = repo.getByAuthKey("coiyubfcqcqeuigcqfuwdwd")
    val users1 = repo.getAll()
    val users2 = repo.getAll(mapOf(
        "name" to "Ayman Hamdoun"
    ))

    Log.logger.info("Application Started")

    initScheduledJobs();

    embeddedServer(Netty, port = 8080) {
       configureRouting();
    }.start(wait = true)
}


fun initScheduledJobs() {
    GlobalScope.launch {
        while(true) {
            Log.logger.info("JOB: Lira Job STARTED")
            LiraRateJob.getAndSaveJobData()
            Log.logger.info("JOB: Lira Job ENDED")

            Thread.sleep(5 * 60 * 1000)
        }
    }
}