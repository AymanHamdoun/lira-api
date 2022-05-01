package lira.ahamdoun.models

class User (
    private val id: Int,
    private val name: String,
    private val email: String,
    private val authKey: String,
    private val status: Boolean,
) : BaseModel() {

    fun getID(): Int {return id}
    fun getName(): String {return name}
    fun getEmail(): String {return email}
    fun getAuthKey(): String {return authKey}
    fun isEmailConfirmed(): Boolean {return status}
}