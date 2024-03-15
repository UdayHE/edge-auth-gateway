package io.github.udayhe.edgeauthgateway.oidc;


import io.github.udayhe.edgeauthgateway.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

import static io.github.udayhe.edgeauthgateway.utils.Constants.AUTHORIZATION_REQUEST_URI;
import static io.github.udayhe.edgeauthgateway.utils.TenantResolver.extractRegistrationIdFromURL;


/**
 *
 * @author udayhegde
 */

@Slf4j
@AllArgsConstructor
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

    private final ReactiveClientRegistrationRepository clientRegistrationRepository;


    private final ServerRequestCache requestCache = new WebSessionServerRequestCache();


    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        log.info("Inside CustomAuthenticationEntryPoint");

        // Use ServerRequestCache to save the request for later
        return requestCache.saveRequest(exchange)
                .then(exchange.getSession())
                .flatMap(session -> {
                    // Save the original request URL in session for custom use
                    String originalUrl = exchange.getRequest().getURI().toString();
                    session.getAttributes().put(Constants.ORIGINAL_REQUEST_URL, originalUrl);
                    String springSecurityUrl = (String) session.getAttributes().get(Constants.SPRING_SECURITY_SAVED_REQUEST);
                    if (log.isDebugEnabled()) {
                        log.debug("Original URL saved in session: {}", originalUrl);
                        log.debug("Spring Security saved request URL in session: {}", springSecurityUrl);
                    }
                    String provider = extractRegistrationIdFromURL(exchange.getRequest().getURI());
              //      String tenant = extractTenantFromURL(exchange.getRequest().getURI());
                    return Mono.just(provider)
                            .flatMap(clientRegistrationRepository::findByRegistrationId)
                            .flatMap(clientRegistration -> {
                                String redirectUrl = UriComponentsBuilder.fromUriString(AUTHORIZATION_REQUEST_URI)
                                        .pathSegment(clientRegistration.getRegistrationId())
                                        .build()
                                        .toUriString();
                                exchange.getResponse().setStatusCode(HttpStatus.FOUND);
                                exchange.getResponse().getHeaders().setLocation(URI.create(redirectUrl));
                                return exchange.getResponse().setComplete();
                            })
                            .switchIfEmpty(Mono.defer(() -> {
                                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                                return exchange.getResponse().setComplete();
                            }));
                });
    }
}


