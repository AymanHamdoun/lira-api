package lira.ahamdoun.repositories

import lira.ahamdoun.models.BaseModel
import lira.ahamdoun.models.User
import lira.ahamdoun.utility.Database
import lira.ahamdoun.utility.InsertResult
import java.sql.ResultSet

class UserRepository : BaseRepository() {

    private val COLUMN_ID = "id";
    private val COLUMN_AUTH_KEY = "auth_key";

    override fun getTableName(): String {return "users"}

    fun getByID(id: Int): User {
        return getFirstByColumn(this.COLUMN_ID, id) as User
    }

    fun getByAuthKey(authKey: String): User {
        return getFirstByColumn(this.COLUMN_AUTH_KEY, authKey) as User
    }

    fun saveNew(user: User): InsertResult {
        return save(mapOf<String, Any>(
            "name" to user.getName(),
            "email" to user.getEmail(),
            "auth_key" to user.getAuthKey(),
            "confirmation_hash" to user.getConfirmationHash()
        ))
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