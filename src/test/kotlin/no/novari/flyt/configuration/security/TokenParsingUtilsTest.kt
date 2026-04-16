package no.novari.flyt.configuration.security

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class TokenParsingUtilsTest {
    private lateinit var tokenParsingUtils: TokenParsingUtils

    @BeforeEach
    fun setUp() {
        tokenParsingUtils = TokenParsingUtils()
    }

    @Test
    fun shouldReturnEmptyWhenAuthenticationIsNotJwt() {
        val authentication = mock<Authentication>()

        val result = tokenParsingUtils.tryGetAuditorName(authentication)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnEmptyWhenAttributesAreMissing() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(mapOf())

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnDisplayNameWhenDisplayNameAndEmailArePresent() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "John Doe",
                "email" to "john@example.com",
            ),
        )

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isEqualTo("John Doe")
    }

    @Test
    fun shouldReturnDisplayNameWhenPrincipalNameIsMissingButNameExists() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "Jane Smith",
            ),
        )

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isEqualTo("Jane Smith")
    }

    @Test
    fun shouldReturnNullWhenNameAndEmailAttributesAreBlank() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "  ",
                "email" to " ",
                "preferred_username" to " ",
                "principalName" to " ",
            ),
        )

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnEmailWhenOnlyEmailIsPresent() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "email" to "test@domain.no",
            ),
        )

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isEqualTo("test@domain.no")
    }

    @Test
    fun shouldPreferDisplayNameOverEmailBasedFields() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "Jane Smith",
                "email" to "jane.smith@org.no",
                "preferred_username" to "jsmith",
                "principalName" to "fallback@org.no",
            ),
        )

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isEqualTo("Jane Smith")
    }

    @Test
    fun shouldReturnAuditorNameWhenOidIsMissing() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "Jane Smith",
                "email" to "jane.smith@org.no",
            ),
        )

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isEqualTo("Jane Smith")
    }

    @Test
    fun shouldReturnEmailAsAuditorNameWhenOnlyPrincipalNameIsPresent() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "principalName" to "jane.smith@org.no",
            ),
        )

        val result = tokenParsingUtils.tryGetAuditorName(jwt)

        assertThat(result).isEqualTo("jane.smith@org.no")
    }

    @Test
    fun shouldConvertToTitleCaseCorrectly() {
        val input = "john DOE"
        val result = invokeToTitleCase(input)
        assertThat(result).isEqualTo("John Doe")
    }

    @Test
    fun shouldPreserveNonLetterCharactersInTitleCase() {
        val input = "anne-marie o’neill"
        val result = invokeToTitleCase(input)
        assertThat(result).isEqualTo("Anne-Marie O’Neill")
    }

    private fun invokeToTitleCase(input: String): String =
        try {
            val method = TokenParsingUtils::class.java.getDeclaredMethod("toTitleCase", String::class.java)
            method.isAccessible = true
            method.invoke(tokenParsingUtils, input) as String
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
}
