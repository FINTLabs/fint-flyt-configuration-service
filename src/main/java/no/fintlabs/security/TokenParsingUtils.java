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

import static org.springframework.util.StringUtils.hasText;

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
        if (!hasText(input)) return input;

        StringBuilder result = new StringBuilder(input.length());
        boolean startOfWord = true;

        for (char c : input.trim().toCharArray()) {
            if (Character.isLetter(c)) {
                result.append(startOfWord ? Character.toTitleCase(c) : Character.toLowerCase(c));
                startOfWord = false;
            } else {
                result.append(c);
                startOfWord = Character.isWhitespace(c) || c == '-' || c == '\'' || c == '’';
            }
        }
        return result.toString();
    }

    public Optional<User> tryGetUser(Authentication authentication) {
        if (!(authentication instanceof JwtAuthenticationToken jwt)) return Optional.empty();

        Map<String, Object> attrs = jwt.getTokenAttributes();

        String name = firstNonBlank(
                text(attrs.get("displayname")),
                text(attrs.get("name")),
                text(attrs.get("preferred_username")),
                text(attrs.get("principalName"))
        );

        String email = firstNonBlank(
                text(attrs.get("email")),
                text(attrs.get("principalName"))
        );

        UUID oid = parseUuid(text(attrs.get("objectidentifier")));

        if ((name == null && email == null) || oid == null) return Optional.empty();

        return Optional.of(User.builder()
                .objectIdentifier(oid)
                .name(name)
                .email(email)
                .build());
    }

    private static String text(Object o) {
        return Optional.ofNullable(o)
                .map(Object::toString)
                .map(String::strip)
                .filter(StringUtils::hasText)
                .orElse(null);
    }

    private static String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (hasText(v)) return v;
        }
        return null;
    }

    private static UUID parseUuid(String s) {
        try {
            return s == null ? null : UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}