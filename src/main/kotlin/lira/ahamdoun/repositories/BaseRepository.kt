package lira.ahamdoun.repositories

import lira.ahamdoun.models.BaseModel
import lira.ahamdoun.utility.Database
import java.sql.ResultSet

abstract class BaseRepository {
    abstract fun getBaseSelectQuery(): String
    abstract fun getObjectFromResultSet(resultSet: ResultSet): BaseModel

    protected fun getFirstByColumn(columnName: String, columnValue: Any): BaseModel? {
        var model: BaseModel? = null

        Database.select(getBaseSelectQuery() + " WHERE $columnName = ?", mapOf(columnName to columnValue)) { resultSet ->
            model = getObjectFromResultSet(resultSet)
        }

        return model
    }

    fun getAll(): MutableList<BaseModel> {
        return getModels(getBaseSelectQuery())
    }

    fun getAll(conditions: Map<String, Any>, isAnded: Boolean = true): MutableList<BaseModel> {
        val whereCondition = Database.getWhereConditionStringFromMap(conditions, isAnded)
        val sql = getBaseSelectQuery() + " " + whereCondition
        return getModels(sql, conditions)
    }

    private fun getModels(sql: String, parameters: Map<String, Any> = mapOf()): MutableList<BaseModel> {
        val models: MutableList<BaseModel> = mutableListOf()

        Database.select(sql, parameters) { resultSet ->
            val model = getObjectFromResultSet(resultSet)
            models.add(model)
        }

        return models
    }
}