package dk.claudiub.babbeltest.core

import dk.claudiub.babbeltest.api.ErrorCode

class AsyncResource<T : Any>(
    var status: Status,
    var data: T? = null,
    var error: ErrorData? = null
) {
    /** Error information
     *
     */
    class ErrorData(
        var code: Int,
        var message: String,
        var data: Any? = null
    ) {
        constructor(errorCode: ErrorCode, data: Any? = null) : this(
            errorCode.code,
            errorCode.errorText.orEmpty(),
            data
        )
    }

    companion object {
        fun <T : Any> idle() = AsyncResource<T>(Status.IDLE)
        fun <T : Any> loading() = AsyncResource<T>(Status.LOADING)
        fun <T : Any> error(errorData: ErrorData) =
            AsyncResource<T>(Status.ERROR, error = errorData)

        fun <T : Any> success(data: T?) = AsyncResource<T>(Status.SUCCESS, data = data)
    }

    enum class Status {
        IDLE,
        LOADING,
        SUCCESS,
        ERROR
    }

}