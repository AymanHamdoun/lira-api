package lira.ahamdoun.models

class User (
    private val id: Int = -1,
    private val name: String,
    private val email: String,
    private val authKey: String,
    private val status: Boolean,
    private val confirmationHash: String = "",
    private val registrationTimestamp: Int = -1,
    private val confirmationTimestamp: Int = -1,
) : BaseModel() {

    fun getID(): Int {return id}
    fun getName(): String {return name}
    fun getEmail(): String {return email}
    fun getAuthKey(): String {return authKey}
    fun isEmailConfirmed(): Boolean {return status}
    fun getConfirmationHash(): String {return confirmationHash}
}