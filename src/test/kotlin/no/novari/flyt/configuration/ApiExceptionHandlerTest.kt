package no.novari.flyt.configuration

import jakarta.servlet.http.HttpServletRequest
import no.novari.flyt.configuration.validation.CouldNotFindMetadataException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class ApiExceptionHandlerTest {
    private val exceptionHandler = ApiExceptionHandler()

    @Test
    fun shouldReturnExactMessageForResponseStatusException() {
        val request = mock<HttpServletRequest>()
        whenever(request.requestURI).thenReturn("/ofk-no/api/intern/konfigurasjoner/210")

        val response =
            exceptionHandler.handleResponseStatusException(
                ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    "Validation error: ['mapping.valueMappingPerKey contains references to instance values that are not defined in the metadata: ['skjema.Logic.Kommune_til_tittel1']']",
                ),
                request,
            )

        assertEquals(422, response.statusCode.value())
        assertEquals("Unprocessable Entity", response.body?.error)
        assertEquals(
            "Validation error: ['mapping.valueMappingPerKey contains references to instance values that are not defined in the metadata: ['skjema.Logic.Kommune_til_tittel1']']",
            response.body?.message,
        )
        assertEquals("/ofk-no/api/intern/konfigurasjoner/210", response.body?.path)
        assertTrue(response.body?.timestamp != null)
    }

    @Test
    fun shouldReturnExactMessageForMetadataLookupFailure() {
        val request = mock<HttpServletRequest>()
        whenever(request.requestURI).thenReturn("/ofk-no/api/intern/konfigurasjoner/210")

        val response =
            exceptionHandler.handleConfigurationValidationContextException(
                CouldNotFindMetadataException(98L),
                request,
            )

        assertEquals(422, response.statusCode.value())
        assertEquals("Unprocessable Entity", response.body?.error)
        assertEquals("Could not find metadata with id=98", response.body?.message)
        assertEquals("/ofk-no/api/intern/konfigurasjoner/210", response.body?.path)
        assertTrue(response.body?.timestamp != null)
    }
}
