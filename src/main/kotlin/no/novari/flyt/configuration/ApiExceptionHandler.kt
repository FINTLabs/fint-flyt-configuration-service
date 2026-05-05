package no.novari.flyt.configuration

import jakarta.servlet.http.HttpServletRequest
import no.novari.flyt.configuration.validation.CouldNotFindInstanceMetadataException
import no.novari.flyt.configuration.validation.CouldNotFindIntegrationException
import no.novari.flyt.configuration.validation.CouldNotFindMetadataException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestControllerAdvice
class ApiExceptionHandler {
    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(
        exception: ResponseStatusException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> =
        ResponseEntity
            .status(exception.statusCode)
            .body(
                ApiErrorResponse(
                    status = exception.statusCode.value(),
                    error = HttpStatus.valueOf(exception.statusCode.value()).reasonPhrase,
                    message = exception.reason ?: exception.message ?: "Request failed",
                    path = request.requestURI,
                    timestamp = Instant.now(),
                ),
            )

    @ExceptionHandler(
        CouldNotFindIntegrationException::class,
        CouldNotFindMetadataException::class,
        CouldNotFindInstanceMetadataException::class,
    )
    fun handleConfigurationValidationContextException(
        exception: RuntimeException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> =
        ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(
                ApiErrorResponse(
                    status = HttpStatus.UNPROCESSABLE_ENTITY.value(),
                    error = HttpStatus.UNPROCESSABLE_ENTITY.reasonPhrase,
                    message = exception.message ?: "Could not validate configuration",
                    path = request.requestURI,
                    timestamp = Instant.now(),
                ),
            )
}

data class ApiErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: Instant,
)
