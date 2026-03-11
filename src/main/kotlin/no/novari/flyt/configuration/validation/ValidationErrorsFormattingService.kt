package no.novari.flyt.configuration.validation

import jakarta.validation.ConstraintViolation
import org.springframework.stereotype.Service

@Service
class ValidationErrorsFormattingService {
    fun <T> format(errors: Set<ConstraintViolation<T>>): String {
        val sortedErrors =
            errors
                .map { constraintViolation ->
                    val propertyPath = constraintViolation.propertyPath.toString()
                    val propertyPrefix = if (propertyPath.isBlank()) "" else "$propertyPath "
                    "'$propertyPrefix${constraintViolation.message}'"
                }.sorted()

        val prefix = if (errors.size > 1) "Validation errors:" else "Validation error:"
        return "$prefix ${sortedErrors.joinToString(prefix = "[", postfix = "]")}"
    }
}
