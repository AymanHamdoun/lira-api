package lira.ahamdoun.models

import lira.ahamdoun.utility.Database

class User (
    private val id: Int,
    private val name: String,
    private val email: String,
    private val auth_key: String,
    private val status: Boolean,
) {

    fun getName(): String {return name}

    companion object {
        fun getByID(id: Int): User? {
            var user: User? = null

            val results = Database.select("SELECT id, name, email, auth_key, status FROM users WHERE id = ?", { statement ->
                statement.setInt(1, id)
                statement
            }, { resultSet ->
                user = User(resultSet.getInt(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getBoolean(5),
                        )
            })

            return user
        }
    }
}