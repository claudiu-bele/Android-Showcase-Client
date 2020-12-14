package dk.claudiub.babbeltest.api

import dk.claudiub.babbeltest.core.AsyncResource

enum class ErrorCode(val code: Int, val errorText: String? = null) {
    GET_TRANSLATIONS_REPO_UNKNOWN(1),
    GET_TRANSLATIONS_NO_ITEMS(2, "No translation items returned");

    fun toErrorData(data: Any? = null) = AsyncResource.ErrorData(this, data)
}