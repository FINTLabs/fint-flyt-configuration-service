package no.fintlabs.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import no.fintlabs.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenParsingUtils {

    public AuditorScope bindAuditor(Authentication authentication) {
        tryGetUser(authentication)
                .map(u -> firstNonBlank(toTitleCase(u.getName()), u.getEmail()))
                .filter(StringUtils::hasText)
                .ifPresent(CurrentAuditor::set);

        return CurrentAuditor::clear;
    }

    private static String toTitleCase(String input) {
        if (!StringUtils.hasText(input)) return input;

        String s = input.trim();
        StringBuilder out = new StringBuilder(s.length());
        boolean startOfWord = true;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetter(c)) {
                out.append(startOfWord ? Character.toTitleCase(c) : Character.toLowerCase(c));
                startOfWord = false;
            } else {
                out.append(c);
                startOfWord = Character.isWhitespace(c) || c == '-' || c == '\'' || c == '’';
            }
        }
        return out.toString();
    }

    public Optional<User> tryGetUser(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwt)) {
            return Optional.empty();
        }

        Map<String, Object> a = jwt.getTokenAttributes();

        String displayName = text(a.get("displayname"));
        String email       = text(a.get("email"));
        String principal   = text(a.get("principalName"));
        String name        = firstNonBlank(
                displayName,
                text(a.get("name")),
                text(a.get("preferred_username")),
                principal
        );

        UUID oid = parseUuid(text(a.get("objectidentifier")));

        if (name == null && email == null && oid == null) {
            return Optional.empty();
        }

        return Optional.of(
                User.builder()
                        .objectIdentifier(oid)
                        .name(name)
                        .email(email != null ? email : principal)
                        .build()
        );
    }

    private static String text(Object o) {
        if (o == null) return null;
        String s = StringUtils.trimWhitespace(String.valueOf(o));
        return StringUtils.hasText(s) ? s : null;
    }

    private static String firstNonBlank(String... vals) {
        if (vals == null) return null;
        for (String v : vals) {
            if (StringUtils.hasText(v)) return v;
        }
        return null;
    }

    private static UUID parseUuid(String s) {
        if (s == null) return null;
        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException ignore) {
            return null;
        }
    }
}