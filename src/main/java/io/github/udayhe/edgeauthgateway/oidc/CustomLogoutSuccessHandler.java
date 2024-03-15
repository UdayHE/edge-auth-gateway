package io.github.udayhe.edgeauthgateway.oidc;

import io.github.udayhe.edgeauthgateway.utils.TenantResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;

import java.net.URI;

import static io.github.udayhe.edgeauthgateway.utils.Constants.LOCAL_USER_PROFILE_URL;


/**
 *
 * @author udayhegde
 */

public class CustomLogoutSuccessHandler extends OidcClientInitiatedServerLogoutSuccessHandler {

    public CustomLogoutSuccessHandler(ReactiveClientRegistrationRepository reactiveClientRegistrationRepository) {
        super(reactiveClientRegistrationRepository);
    }

    /*

    TODO if using JWT token remove from DB and invalidate Cache "RememberMe"
     */
    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        return exchange.getSession()
                .doOnNext(WebSession::invalidate)
                .then(Mono.defer(() -> {
                    String tenantId = TenantResolver.resolveTenant(exchange); // Implement this method based on your tenant resolution logic
                    String oktaDomain = "https://" + tenantId + ".okta.com";
                    String oktaLogoutUrl = oktaDomain + "/logout?redirect_uri=" + LOCAL_USER_PROFILE_URL;
                    exchange.getResponse().setStatusCode(HttpStatus.SEE_OTHER);
                    exchange.getResponse().getHeaders().setLocation(URI.create(oktaLogoutUrl));
                    return Mono.empty();
                }));
    }

}
