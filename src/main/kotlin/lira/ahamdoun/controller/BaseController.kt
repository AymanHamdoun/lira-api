package lira.ahamdoun.controller

import io.ktor.http.*
import lira.ahamdoun.models.User

open class BaseController (val parameters: Parameters, var authenticatedUser: User? = null)