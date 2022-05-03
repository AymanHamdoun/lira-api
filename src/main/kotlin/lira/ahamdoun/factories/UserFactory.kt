package lira.ahamdoun.factories

import lira.ahamdoun.models.User
import org.apache.commons.codec.digest.DigestUtils

class UserFactory {

    fun create(name: String, email: String): User {
        val timeInMillis = System.currentTimeMillis()
        val authKey = DigestUtils.sha256(email + timeInMillis).toString()
        val confirmationHash = DigestUtils.md5(email + timeInMillis).toString()
        val registrationTimestamp = (timeInMillis / 1000).toInt();

        return User(
            name = name,
            email = email,
            authKey = authKey,
            confirmationHash = confirmationHash,
            registrationTimestamp = registrationTimestamp,
            status = false
        )
    }
}