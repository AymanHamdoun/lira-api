package lira.ahamdoun.jobs

import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import lira.ahamdoun.utility.Log

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.lang.Exception
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializable
data class LiraJobData (
    @SerialName("job_run_time")
    val jobStartTime: String,

    @SerialName("job_run_time_s")
    val jobStartTimeInSeconds: Long,

    @SerialName("rates")
    val rates: List<ExchangeRate>
)

@Serializable
data class ExchangeRate (
    val description: String,
    val source: String,
    var buy: String?,
    var sell: String?,
    var sourceCallTime: String?
)

class LiraRateJob {
    companion object {

        val FILE_PATH = "./lira-job.json"
        val LBP_RATES_WEBSITE_URL = "https://lbprate.com/"

        fun getAndSaveJobData(): LiraJobData {
            val jobStartDateTime = LocalDateTime.now()

            val exchangeRates: MutableList<ExchangeRate> = mutableListOf()

            try {
                exchangeRates.add(getRatesFromLBPRatesWebsite())
            } catch (e: Exception) {
                Log.logger.warn(e.message)
            }

            val jobData = LiraJobData(
                jobStartDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                jobStartDateTime.toEpochSecond(ZoneOffset.UTC),
                exchangeRates
            );

            saveJobDataToFile(jobData)

            return jobData
        }

        private fun getRatesFromLBPRatesWebsite(): ExchangeRate {
            val rate = ExchangeRate("LBP - Fresh USD Rate", LBP_RATES_WEBSITE_URL, null, null, null);


            val timeBeforeCall = System.currentTimeMillis()
            val doc: Document = Jsoup.connect(LBP_RATES_WEBSITE_URL).get()
            rate.sourceCallTime = ((System.currentTimeMillis() - timeBeforeCall).toInt() / 1000).toString() + " Seconds"

            val elements: Elements = doc.select("header div.col-md-4") ?: throw Exception("No Elements Found")

            for (element in elements) {
                val titleParts = element.child(0).children()
                val value = titleParts[1].html()

                if (titleParts[0].html() == "Sell") {
                    rate.sell = value
                } else {
                    rate.buy = value
                }
            }
            return rate
        }

        private fun saveJobDataToFile(jobData: LiraJobData) {
            val fileContents = Json { isLenient = true; prettyPrint = true }.encodeToString(jobData);
            val file = File(FILE_PATH)
            file.writeText(fileContents)
        }
    }
}