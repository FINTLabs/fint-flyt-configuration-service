package no.novari.flyt.configuration.security

import org.springframework.data.domain.AuditorAware
import org.springframework.stereotype.Component
import java.util.Optional

@Component("tokenAuditorAware")
class TokenAuditorAware : AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> = Optional.ofNullable(currentAuditorName())

    private fun currentAuditorName(): String = CurrentAuditor.get().takeUnless(String?::isNullOrBlank) ?: "system"
}
