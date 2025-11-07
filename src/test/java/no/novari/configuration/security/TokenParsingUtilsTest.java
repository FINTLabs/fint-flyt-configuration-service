package no.novari.configuration.security;

import no.novari.configuration.model.User;
import no.novari.configuration.security.TokenParsingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenParsingUtilsTest {

    private TokenParsingUtils tokenParsingUtils;

    @BeforeEach
    void setUp() {
        tokenParsingUtils = new TokenParsingUtils();
    }

    @Test
    void shouldReturnEmptyWhenAuthenticationIsNotJwt() {
        Authentication authentication = mock(Authentication.class);

        Optional<User> result = tokenParsingUtils.tryGetUser(authentication);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenAttributesAreMissing() {
        JwtAuthenticationToken jwt = mock(JwtAuthenticationToken.class);
        when(jwt.getTokenAttributes()).thenReturn(Map.of());

        Optional<User> result = tokenParsingUtils.tryGetUser(jwt);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnUserWhenDisplayNameEmailAndOidArePresent() {
        UUID oid = UUID.randomUUID();
        JwtAuthenticationToken jwt = mock(JwtAuthenticationToken.class);
        when(jwt.getTokenAttributes()).thenReturn(Map.of(
                "displayname", "John Doe",
                "email", "john@example.com",
                "objectidentifier", oid.toString()
        ));

        Optional<User> result = tokenParsingUtils.tryGetUser(jwt);

        assertThat(result).isPresent();
        User user = result.get();
        assertThat(user.getName()).isEqualTo("John Doe");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
        assertThat(user.getObjectIdentifier()).isEqualTo(oid);
    }

    @Test
    void shouldReturnEmptyWhenEmailIsMissing() {
        JwtAuthenticationToken jwt = mock(JwtAuthenticationToken.class);
        when(jwt.getTokenAttributes()).thenReturn(Map.of(
                "displayname", "Jane Smith",
                "principalName", "jane.smith@org.no"
        ));

        Optional<User> result = tokenParsingUtils.tryGetUser(jwt);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenOidIsInvalid() {
        JwtAuthenticationToken jwt = mock(JwtAuthenticationToken.class);
        when(jwt.getTokenAttributes()).thenReturn(Map.of(
                "displayname", "Broken UUID",
                "objectidentifier", "not-a-uuid"
        ));

        Optional<User> result = tokenParsingUtils.tryGetUser(jwt);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldParseUserWhenOnlyEmailAndOidArePresent() {
        UUID oid = UUID.randomUUID();
        JwtAuthenticationToken jwt = mock(JwtAuthenticationToken.class);
        when(jwt.getTokenAttributes()).thenReturn(Map.of(
                "email", "test@domain.no",
                "objectidentifier", oid.toString()
        ));

        Optional<User> result = tokenParsingUtils.tryGetUser(jwt);

        assertThat(result).isPresent();
        User user = result.get();
        assertThat(user.getName()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@domain.no");
        assertThat(user.getObjectIdentifier()).isEqualTo(oid);
    }

    @Test
    void shouldIgnoreBlankOrWhitespaceAttributes() {
        UUID oid = UUID.randomUUID();
        JwtAuthenticationToken jwt = mock(JwtAuthenticationToken.class);
        when(jwt.getTokenAttributes()).thenReturn(Map.of(
                "displayname", "  ",
                "email", " ",
                "objectidentifier", oid.toString()
        ));

        Optional<User> result = tokenParsingUtils.tryGetUser(jwt);
        assertThat(result).isEmpty();
    }

    @Test
    void shouldConvertToTitleCaseCorrectly() {
        String input = "john DOE";
        String result = invokeToTitleCase(input);
        assertThat(result).isEqualTo("John Doe");
    }

    @Test
    void shouldPreserveNonLetterCharactersInTitleCase() {
        String input = "anne-marie o’neill";
        String result = invokeToTitleCase(input);
        assertThat(result).isEqualTo("Anne-Marie O’Neill");
    }

    private String invokeToTitleCase(String input) {
        try {
            var method = TokenParsingUtils.class.getDeclaredMethod("toTitleCase", String.class);
            method.setAccessible(true);
            return (String) method.invoke(tokenParsingUtils, input);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}