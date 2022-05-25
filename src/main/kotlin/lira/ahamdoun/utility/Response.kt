package lira.ahamdoun.utility

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

abstract class Response {
    protected val jsonBuilder = Json {
        prettyPrint = true
        isLenient = true
    }

    abstract fun json(): String
}


class GeneralResponse(val response: GeneralResponseData) : Response() {
    companion object {
        fun ok(): GeneralResponse {
            return GeneralResponse(GeneralResponseData(GeneralResponseData.STATUS_OK, 200, GeneralResponseData.MSG_SUCCESS))
        }

        fun error(msg: String = GeneralResponseData.MSG_ERROR, code: Int = 500) : GeneralResponse {
            return GeneralResponse(GeneralResponseData(GeneralResponseData.STATUS_ERROR, code, msg))
        }
    }

    override fun json(): String {
        return jsonBuilder.encodeToString(response)
    }
}

@Serializable
data class GeneralResponseData(val status: String, val code: Int, val message: String) {
    companion object {
        const val STATUS_OK = "ok"
        const val STATUS_ERROR = "error"

        const val MSG_SUCCESS = "success"
        const val MSG_ERROR = "General Error"
    }
}