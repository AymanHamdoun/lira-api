package lira.ahamdoun.controller

import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import lira.ahamdoun.jobs.LiraJobData
import lira.ahamdoun.jobs.LiraRateJob
import lira.ahamdoun.utility.GeneralResponseData.Companion.MSG_SUCCESS
import lira.ahamdoun.utility.GeneralResponseData.Companion.STATUS_OK
import lira.ahamdoun.utility.GeneralResponse
import lira.ahamdoun.utility.Log
import lira.ahamdoun.utility.Response
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset


@Serializable
data class LiraRateResponseData (
    val status: String,
    val code: Int,
    val message: String,
    var source: String,
    var data: LiraJobData?
)

class LiraRateResponse (val response: LiraRateResponseData) : Response() {
    override fun json(): String {
        return jsonBuilder.encodeToString(response)
    }
}

class LiraRatesController(parameters: Parameters) : BaseController(parameters) {

    fun getLiraRate(): String {
        try {
            val responseData = LiraRateResponseData(
                STATUS_OK,
                200,
                MSG_SUCCESS,
                "Cache",
                null
            )
            fillResponseData(responseData)

            if (responseData.data == null) {
                return GeneralResponse.error("Data Unavailable", 404).json()
            }

            return LiraRateResponse(responseData).json()
        } catch (e: Exception) {
            println(e.stackTraceToString())
            return GeneralResponse.error(e.toString()).json()
        }
    }

    private fun fillResponseData(responseData: LiraRateResponseData) {
        val file = File(LiraRateJob.FILE_PATH)
        var jobData: LiraJobData? = null

        val json = Json {
            prettyPrint = true
            isLenient = true
        }

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
        return ((LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) - jobData.jobStartTimeInSeconds) > 60 * 5)
    }
}