package products.controller.model

import org.slf4j.Logger
import org.slf4j.LoggerFactory

data class ErrorResponse(val error: String)

fun Exception.toErrorResponse() =
    ErrorResponse(error = messageForErrorResponse())

private fun Exception.messageForErrorResponse() =
    message ?: "Unexpected Exception: ${this::class.simpleName}".also {
        logger.error(it) { "Exception is missing message. $it" }
    }

val logger: Logger = LoggerFactory.getLogger(ErrorResponse::class.java)