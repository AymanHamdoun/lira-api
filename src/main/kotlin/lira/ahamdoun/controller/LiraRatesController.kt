package lira.ahamdoun.controller

import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lira.ahamdoun.jobs.LiraJobData
import lira.ahamdoun.jobs.LiraRateJob
import lira.ahamdoun.utility.Log
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset


@Serializable
data class ErrorResponse (val status: Int, val message: String)

@Serializable
data class LiraRateResponse(val status: Int, var source: String, var data: LiraJobData?)

class LiraRatesController(parameters: Parameters) : BaseController(parameters) {

    private val json = Json {
        prettyPrint = true
        isLenient = true
    }

    fun getLiraRate(): String {
        return try {
            val responseData = LiraRateResponse(200, "Cache", null)
            fillResponseData(responseData)

            if (responseData.data == null) {
                return json.encodeToString(ErrorResponse(500, "LBP Rate Data Not Found"))
            }

            json.encodeToString(responseData)
        } catch (e: Exception) {
            println(e.stackTraceToString())
            json.encodeToString(ErrorResponse(500, e.toString()))
        }
    }

    private fun fillResponseData(responseData: LiraRateResponse) {
        val file = File(LiraRateJob.FILE_PATH)
        var jobData: LiraJobData? = null

        // create the job data object from previously saved data in the JSON file
        if (file.exists()) {
            val jobDataJSON = file.readText().trimIndent()
            jobData = json.decodeFromString<LiraJobData>(jobDataJSON)
        }

        // if the file doesn't exist OR it does but is outdated,
        // run the job to re-create it and get the data
        if (jobData == null || isOutdatedJobData(jobData)) {
            jobData = LiraRateJob.getAndSaveJobData()
            responseData.source = "Web Call"
            Log.logger.info("Had to issue a web call")
        }

        responseData.data = jobData
    }

    private fun isOutdatedJobData(jobData: LiraJobData): Boolean {
        return false && ((LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - jobData.jobStartTimeInSeconds) > 60 * 5)
    }
}