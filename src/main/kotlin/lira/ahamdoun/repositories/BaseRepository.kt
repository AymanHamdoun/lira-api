package lira.ahamdoun.repositories

import lira.ahamdoun.models.BaseModel
import lira.ahamdoun.utility.Database
import java.sql.PreparedStatement
import java.sql.ResultSet

abstract class BaseRepository {
    abstract fun getBaseSelectQuery(): String
    abstract fun getObjectFromResultSet(resultSet: ResultSet): BaseModel

    protected fun getByIntColumn(columnName: String, columnValue: Int): BaseModel? {
        var model: BaseModel? = null

        Database.select(getBaseSelectQuery() + " WHERE $columnName = ?", { statement ->
            statement.setInt(1, columnValue)
            statement
        }, { resultSet ->
            model = getObjectFromResultSet(resultSet)
        })

        return model
    }

    protected fun getByStringColumn(columnName: String, columnValue: String): BaseModel? {
        var model: BaseModel? = null

        Database.select(getBaseSelectQuery() + " WHERE $columnName = ?", { statement ->
            statement.setString(1, columnValue)
            statement
        }, { resultSet ->
            model = getObjectFromResultSet(resultSet)
        })

        return model
    }
    fun getAll(): MutableList<BaseModel> {
        return getAllByPreparedStatement(getBaseSelectQuery()) { statement -> statement }
    }

    fun getAll(conditions: Map<String, Any>): MutableList<BaseModel> {
        val whereCondition = Database.getWhereConditionStringFromMap(conditions)
        val sql = getBaseSelectQuery() + " " + whereCondition
        return getAllByPreparedStatement(sql) { statement ->
            Database.fillPreparedStatementWithMapConditions(statement, conditions)
            statement
        }
    }

    private fun getAllByPreparedStatement(sql: String, statementHandler: (statement: PreparedStatement) -> PreparedStatement): MutableList<BaseModel> {
        val models: MutableList<BaseModel> = mutableListOf()

        Database.select(sql, { statement ->
            statementHandler(statement)
            statement
        }, { resultSet ->
            val model = getObjectFromResultSet(resultSet)
            models.add(model)
        })

        return models
    }
}