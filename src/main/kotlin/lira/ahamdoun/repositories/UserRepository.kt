package lira.ahamdoun.repositories

import lira.ahamdoun.models.BaseModel
import lira.ahamdoun.models.User
import java.sql.ResultSet

class UserRepository : BaseRepository() {

    private val COLUMN_AUTH_KEY = "auth_key";

    fun getByID(id: Int): User {
        return getByIntColumn("id", id) as User
    }

    fun getByAuthKey(authKey: String): User {
        return getByStringColumn(this.COLUMN_AUTH_KEY, authKey) as User
    }

    override fun getBaseSelectQuery(): String {
        return "SELECT id, name, email, auth_key, status FROM users"
    }

    override fun getObjectFromResultSet(resultSet: ResultSet): BaseModel {
        return User(resultSet.getInt(1),
            resultSet.getString(2),
            resultSet.getString(3),
            resultSet.getString(4),
            resultSet.getBoolean(5),
        )
    }
}