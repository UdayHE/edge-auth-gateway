package io.github.udayhe.edgeauthgateway.webfilter;

import io.github.udayhe.edgeauthgateway.dao.TenantConfig;
import io.github.udayhe.edgeauthgateway.service.TenantConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.net.URI;

import static io.github.udayhe.edgeauthgateway.utils.NullSafeUtils.nullSafeGet;
import static org.apache.logging.log4j.util.Strings.EMPTY;

/**
 *
 * @author udayhegde
 */
@Slf4j
public class TenantIdentificationWebFilter implements WebFilter, Ordered {

    @Autowired
    private TenantConfigService tenantConfigService;
    @Override
    public int getOrder() {
        // Specify the order of the filter
        // The lower the number, the earlier this filter is executed in the chain.
        // Adjust this value based on where you want this filter to execute in relation to other filters.
        return -100;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String tId = exchange.getRequest().getQueryParams().getFirst("provider");
      //  exchange.getAttributes().put("SPRING_SECURITY_TENANT_ID", tId);
        String path = exchange.getRequest().getPath().toString();
        log.info("In TenantIdentificationWebFilter, tId:{}, path:{}", tId, path);
        // Pattern to match: /oauth2/authorization/{tenantId}
        if (path.startsWith("/custom-login")) {
            log.info("In TenantIdentificationWebFilter, tId:{}", tId);
            String tenantId = "hello";
            String redirectUri = determineRedirectUriForTenant(tenantId);

            exchange.getResponse().setStatusCode(HttpStatus.FOUND);
            exchange.getResponse().getHeaders().setLocation(URI.create(redirectUri));

            return exchange.getResponse().setComplete();
        }


        return chain.filter(exchange);
    }

    private String determineRedirectUriForTenant(String tenantId) {
        // Implement your logic to determine the redirect URI based on the tenant ID
        // This example simply redirects to a mocked URI; replace it with actual logic
        TenantConfig tenantConfig = tenantConfigService.getTenantProperties(tenantId).orElse(null);
        String url = String.format("%s/oauth2/auth?client_id=%s",
                nullSafeGet(tenantConfig, TenantConfig::getDomain, EMPTY),
                tenantId);
        log.info("In determineRedirectUriForTenant, url:{}", url);
        return url;
    }
}
