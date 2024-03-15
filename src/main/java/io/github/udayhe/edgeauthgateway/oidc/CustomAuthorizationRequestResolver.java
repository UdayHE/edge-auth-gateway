package io.github.udayhe.edgeauthgateway.oidc;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * @author udayhegde
 */

@AllArgsConstructor
@Slf4j
public class CustomAuthorizationRequestResolver implements ServerOAuth2AuthorizationRequestResolver {

    private final ServerOAuth2AuthorizationRequestResolver defaultResolver;

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
        return defaultResolver.resolve(exchange)
                .map(this::customizeAuthorizationRequest);
    }

    @Override
    public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
        return defaultResolver.resolve(exchange, clientRegistrationId)
                .map(this::customizeAuthorizationRequest);
    }

    private OAuth2AuthorizationRequest customizeAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest) {
        String tenantId = "test-tenant";
        return OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(additionalParameters -> additionalParameters.put("tenant_id", tenantId)).build();
    }
}
