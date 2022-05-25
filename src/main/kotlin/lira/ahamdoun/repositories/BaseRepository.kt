package lira.ahamdoun.repositories

import lira.ahamdoun.models.BaseModel
import lira.ahamdoun.utility.Database
import lira.ahamdoun.utility.InsertResult
import java.sql.ResultSet

abstract class BaseRepository {
    abstract fun getBaseSelectQuery(): String
    abstract fun getObjectFromResultSet(resultSet: ResultSet): BaseModel
    abstract fun getTableName(): String

    protected fun getFirstByColumn(columnName: String, columnValue: Any): BaseModel? {
        var model: BaseModel? = null

        Database.select(getBaseSelectQuery() + " WHERE $columnName = ?", mapOf(columnName to columnValue)) { resultSet ->
            model = getObjectFromResultSet(resultSet)
        }

        return model
    }

    protected fun getAll(): MutableList<BaseModel> {
        return getModels(getBaseSelectQuery())
    }

    protected fun getAll(conditions: Map<String, Any>, isAnded: Boolean = true): MutableList<BaseModel> {
        val whereCondition = Database.getWhereConditionStringFromMap(conditions, isAnded)
        val sql = getBaseSelectQuery() + " " + whereCondition
        return getModels(sql, conditions)
    }

    protected fun update(data: Map<String, Any>, conditions: Map<String, Any>, conditionsAreAnded: Boolean = true): Boolean {
        val whereCondition = Database.getWhereConditionStringFromMap(conditions, conditionsAreAnded)

        val setStrings = mutableListOf<String>()

        for (entry in data.entries.iterator()) {
            setStrings.add("${entry.key}=?")
        }

        val sql = "UPDATE ${getTableName()} SET " + setStrings.joinToString(",") + " $whereCondition"

        return Database.update(sql, data, conditions)
    }

    protected fun save(data: Map<String, Any>): InsertResult {
        val questionMarks = Array<String>(data.size) { "?" }.joinToString(",")
        val sql = "INSERT INTO ${getTableName()} (${data.keys.joinToString(",")}) VALUES ($questionMarks)"
        return Database.insert(sql, data)
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