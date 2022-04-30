package lira.ahamdoun.utility

import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

class Log {
    companion object {
        var logger: Logger? = null

        fun initLogger() {
            val logger = Logger.getLogger(this::class.java.name)
            val simpleFormatter = SimpleFormatter()

            val fileHandler = FileHandler("./lira-api.log", true)
            fileHandler.formatter = simpleFormatter

            logger.addHandler(fileHandler)

            Log.logger = logger
        }
    }
}