package lira.ahamdoun.utility

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.util.*

class Database {
    companion object {
        var logger: Logger = LoggerFactory.getLogger(this::class.java.name)

        val LIRADB_HOST = System.getenv("LIRADB_HOST") ?: ""
        val LIRADB_PORT = System.getenv("LIRADB_PORT") ?: ""
        val LIRADB_NAME = System.getenv("LIRADB_NAME") ?: ""

        @JvmName("getConnection1")
        fun getConnection(): Connection? {
            val props = Properties()
            props["user"] = System.getenv("LIRADB_USER") ?: ""
            props["password"] = System.getenv("LIRADB_PASS") ?: ""

            val connectionString = "jdbc:mysql://$LIRADB_HOST:$LIRADB_PORT/$LIRADB_NAME?useSSL=false"

            var connection: Connection? = null;
            try {
                connection = DriverManager.getConnection(connectionString, props)
            } catch (e: Exception) {
                logger.error(e.message)
                logger.error(e.stackTraceToString())
            }

            return connection
        }

        fun select(sql: String, statementHandler: (statement: PreparedStatement) -> PreparedStatement, resultRowHandler: (resultSet: ResultSet) -> Unit): ResultSet {
            val connection = getConnection() ?: throw Exception("Could not establish database connection")

            var statement = connection.prepareStatement(sql)
            statement = statementHandler(statement)

            val resultSet = statement.executeQuery()
            while(resultSet.next()) {
                resultRowHandler(resultSet)
            }
            resultSet.close()
            statement.close()
            connection.close()
            return resultSet
        }
    }
}