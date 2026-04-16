package no.novari.flyt.configuration.security

import no.novari.flyt.configuration.model.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.util.UUID

class TokenParsingUtilsTest {
    private lateinit var tokenParsingUtils: TokenParsingUtils

    @BeforeEach
    fun setUp() {
        tokenParsingUtils = TokenParsingUtils()
    }

    @Test
    fun shouldReturnEmptyWhenAuthenticationIsNotJwt() {
        val authentication = mock<Authentication>()

        val result: User? = tokenParsingUtils.tryGetUser(authentication)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnEmptyWhenAttributesAreMissing() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(mapOf())

        val result: User? = tokenParsingUtils.tryGetUser(jwt)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnUserWhenDisplayNameEmailAndOidArePresent() {
        val oid = UUID.randomUUID()
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "John Doe",
                "email" to "john@example.com",
                "objectidentifier" to oid.toString(),
            ),
        )

        val result = tokenParsingUtils.tryGetUser(jwt)

        val user = requireNotNull(result)
        assertThat(user.name).isEqualTo("John Doe")
        assertThat(user.email).isEqualTo("john@example.com")
        assertThat(user.objectIdentifier).isEqualTo(oid)
    }

    @Test
    fun shouldReturnEmptyWhenEmailIsMissing() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "Jane Smith",
                "principalName" to "jane.smith@org.no",
            ),
        )

        val result = tokenParsingUtils.tryGetUser(jwt)

        assertThat(result).isNull()
    }

    @Test
    fun shouldReturnEmptyWhenOidIsInvalid() {
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "Broken UUID",
                "objectidentifier" to "not-a-uuid",
            ),
        )

        val result = tokenParsingUtils.tryGetUser(jwt)

        assertThat(result).isNull()
    }

    @Test
    fun shouldParseUserWhenOnlyEmailAndOidArePresent() {
        val oid = UUID.randomUUID()
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "email" to "test@domain.no",
                "objectidentifier" to oid.toString(),
            ),
        )

        val result = tokenParsingUtils.tryGetUser(jwt)

        val user = requireNotNull(result)
        assertThat(user.name).isNull()
        assertThat(user.email).isEqualTo("test@domain.no")
        assertThat(user.objectIdentifier).isEqualTo(oid)
    }

    @Test
    fun shouldIgnoreBlankOrWhitespaceAttributes() {
        val oid = UUID.randomUUID()
        val jwt = mock<JwtAuthenticationToken>()
        whenever(jwt.tokenAttributes).thenReturn(
            mapOf(
                "displayname" to "  ",
                "email" to " ",
                "objectidentifier" to oid.toString(),
            ),
        )

        val result = tokenParsingUtils.tryGetUser(jwt)
        assertThat(result).isNull()
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
