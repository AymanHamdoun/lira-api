package lira.ahamdoun.controller

import io.ktor.http.*
import lira.ahamdoun.factories.UserFactory
import lira.ahamdoun.repositories.UserRepository

class UserController {

    fun register(parameters: Parameters): String {
        val repo = UserRepository()
        val factory = UserFactory()
        repo.saveNew(factory.create("Ayman", "ayman@localhost.com"))
        return ""
    }


}