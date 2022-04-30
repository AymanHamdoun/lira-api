package lira.ahamdoun.utility

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class Log {
    companion object {
        var logger: Logger = LoggerFactory.getLogger(this::class.java.name)
    }
}