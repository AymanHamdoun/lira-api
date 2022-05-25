package lira.ahamdoun.controller

import io.ktor.http.*
import lira.ahamdoun.factories.UserFactory
import lira.ahamdoun.repositories.UserRepository

class UserController(parameters: Parameters) : BaseController(parameters) {

    fun register(): String {
        val repo = UserRepository()
        val factory = UserFactory()

        val name = this.parameters["name"]
        val email = this.parameters["email"]

        if (name != null && email != null) {
            repo.saveNew(factory.create(name, email))
        }
        return ""
    }


}