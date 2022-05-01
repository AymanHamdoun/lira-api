package lira.ahamdoun.repositories

import lira.ahamdoun.models.BaseModel
import lira.ahamdoun.utility.Database
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
}