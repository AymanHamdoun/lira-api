package lira.ahamdoun.utility

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.*
import java.util.*

data class InsertResult(val succeeded: Boolean, val affectedRowCount: Int, val lastInsertID: Long?)

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

        fun select(sql: String, parameters: Map<String, Any>, resultRowHandler: (resultSet: ResultSet) -> Unit) {
            val connection = getConnection() ?: throw Exception("Could not establish database connection")

            val statement = connection.prepareStatement(sql)
            fillPreparedStatementWithMapParameters(statement, parameters)

            val resultSet = statement.executeQuery()
            while(resultSet.next()) {
                resultRowHandler(resultSet)
            }
            resultSet.close()
            statement.close()
            connection.close()
        }

        fun insert(sql: String, parameters: Map<String, Any>): InsertResult {
            val connection = getConnection() ?: throw Exception("Could not establish database connection")
            val statement = connection.prepareStatement(sql)
            fillPreparedStatementWithMapParameters(statement, parameters)
            val affectedRowCount = statement.executeUpdate()
            val lastInsertID: Long?
            statement.generatedKeys.use { generatedKeys ->
                if (generatedKeys.next()) {
                    lastInsertID = generatedKeys.getLong(1)
                } else {
                    throw SQLException("Creating record failed, no ID obtained.")
                }
            }

            return InsertResult((affectedRowCount > 0), affectedRowCount, lastInsertID)
        }

        private fun fillPreparedStatementWithMapParameters(statement: PreparedStatement, parameters: Map<String, Any>) {
            var colIndex = 1;
            for( (_, columnValue) in parameters.entries) {
                when (columnValue) {
                    is String -> {
                        statement.setString(colIndex++, columnValue.toString())
                    }
                    is Int -> {
                        statement.setInt(colIndex++, columnValue.toString().toInt())
                    }
                    is Double -> {
                        statement.setDouble(colIndex++, columnValue.toString().toDouble())
                    }
                }
            }
        }

        fun getWhereConditionStringFromMap(conditions: Map<String, Any>, conditionsAreAnded: Boolean = true): String {
            if (conditions.isEmpty()) {
                return ""
            }

            val separator = if (conditionsAreAnded) " AND " else " OR "

            val conditionStrings: MutableList<String> = mutableListOf()

            for( (key, _) in conditions.entries) {
                conditionStrings.add("$key=?")
            }

            return "WHERE " + conditionStrings.joinToString(separator)
        }
    }
}