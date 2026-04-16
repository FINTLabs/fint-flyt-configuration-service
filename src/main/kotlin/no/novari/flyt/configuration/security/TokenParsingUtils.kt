package no.novari.flyt.configuration.security

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class TokenParsingUtils {
    fun bindAuditor(authentication: Authentication): AuditorScope {
        tryGetAuditorName(authentication)
            ?.takeUnless(String::isBlank)
            ?.let(CurrentAuditor::set)

        return AuditorScope { CurrentAuditor.clear() }
    }

    fun tryGetAuditorName(authentication: Authentication): String? {
        if (authentication !is JwtAuthenticationToken) {
            return null
        }

        val attrs = authentication.tokenAttributes
        val displayName =
            firstNonBlank(
                text(attrs["displayname"]),
                text(attrs["name"]),
            )
        val email =
            firstNonBlank(
                text(attrs["email"]),
                text(attrs["preferred_username"]),
                text(attrs["principalName"]),
            )

        return firstNonBlank(toTitleCase(displayName), email)
    }

    private fun text(value: Any?): String? = value?.toString()?.trim()?.takeUnless(String::isBlank)

    private fun firstNonBlank(vararg values: String?): String? = values.firstOrNull { !it.isNullOrBlank() }

    private companion object {
        @JvmStatic
        private fun toTitleCase(input: String?): String? {
            val trimmed = input?.trim().takeUnless(String?::isNullOrBlank) ?: return input
            val result = StringBuilder(trimmed.length)
            var startOfWord = true

            for (char in trimmed) {
                if (char.isLetter()) {
                    result.append(if (startOfWord) char.titlecaseChar() else char.lowercaseChar())
                    startOfWord = false
                } else {
                    result.append(char)
                    startOfWord = char.isWhitespace() || char == '-' || char == '\'' || char == '’'
                }
            }

            return result.toString()
        }
    }
}
