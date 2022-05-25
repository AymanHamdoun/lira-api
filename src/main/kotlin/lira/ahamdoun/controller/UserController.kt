package lira.ahamdoun.controller

import io.ktor.http.*
import lira.ahamdoun.factories.UserFactory
import lira.ahamdoun.models.User
import lira.ahamdoun.repositories.UserRepository
import lira.ahamdoun.utility.GeneralResponseData
import lira.ahamdoun.utility.GeneralResponse
import lira.ahamdoun.utility.Response

class UserController(parameters: Parameters) : BaseController(parameters) {

    fun register(): String {
        return try {
            val repo = UserRepository()
            val factory = UserFactory()

            val name = this.parameters["name"]
            val email = this.parameters["email"]

            if (name != null && email != null) {
                val user = factory.create(name, email)
                repo.saveNew(user)
                // @TODO send activation link via email or something
            }

            GeneralResponse.ok().json()
        } catch (e: Exception) {
            GeneralResponse.error(msg = e.message ?: "").json()
        }
    }


    fun activate(): String {
        val confirmationHash = parameters["key"] ?: return GeneralResponse.error("Invalid Request").json()

        val repo = UserRepository()
        val user = repo.getByConfirmationHash(confirmationHash)

        if (user !is User) {
            return GeneralResponse.error("Invalid Confirmation Link").json()
        }

        val wasActivated = repo.activate(user)

        return GeneralResponse.ok("Authentication Key: " + user.getAuthKey()).json()
    }
}