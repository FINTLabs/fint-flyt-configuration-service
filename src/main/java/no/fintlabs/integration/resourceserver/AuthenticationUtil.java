//package no.fintlabs.integration.resourceserver;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import reactor.core.publisher.Mono;
//
//import java.util.Objects;
//
//public class AuthenticationUtil {
//
//    private AuthenticationUtil() {
//    }
//
//    public static Mono<String> getSub(Mono<Authentication> authentication) {
//        return authentication
//                .filter(Objects::nonNull)
//                .filter(principal -> principal instanceof JwtAuthenticationToken)
//                .map(principal -> (JwtAuthenticationToken) principal)
//                .map(JwtAuthenticationToken::getName);
//    }
//
//}
