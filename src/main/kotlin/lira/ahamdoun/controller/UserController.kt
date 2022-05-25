package lira.ahamdoun.controller

import io.ktor.http.*
import lira.ahamdoun.factories.UserFactory
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
                repo.saveNew(factory.create(name, email))
            }

            GeneralResponse.ok().json()
        } catch (e: Exception) {
            GeneralResponse.error(msg = e.message ?: "").json()
        }
    }


}