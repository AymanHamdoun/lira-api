package lira.ahamdoun

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import lira.ahamdoun.jobs.LiraRateJob
import lira.ahamdoun.models.User
import lira.ahamdoun.plugins.configureRouting
import lira.ahamdoun.repositories.UserRepository
import lira.ahamdoun.utility.Database
import lira.ahamdoun.utility.Log
import java.lang.Exception
import java.util.logging.FileHandler
import java.util.logging.Level
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

fun main() {
    val repo = UserRepository()

    val user = repo.getByID(1)
    val user2 = repo.getByAuthKey("coiyubfcqcqeuigcqfuwdwd")

    println("REPO user By ID: " + user.getName())
    println("REPO user By Auth Key: " + user2.getName())

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