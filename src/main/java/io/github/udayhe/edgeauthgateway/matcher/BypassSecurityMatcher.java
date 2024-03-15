package io.github.udayhe.edgeauthgateway.matcher;

import io.github.udayhe.edgeauthgateway.utils.WhitelistedPaths;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 *
 * @author udayhegde
 */


public class BypassSecurityMatcher implements ServerWebExchangeMatcher {
    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        String bypassParam = exchange.getRequest().getQueryParams().getFirst("bypassSecurity");
        String path = exchange.getRequest().getURI().getPath();
        if ("true".equals(bypassParam) ||
                WhitelistedPaths.PUBLIC_PATHS.contains(path)) {
            return MatchResult.match();
        }
        return MatchResult.notMatch();
    }
}
