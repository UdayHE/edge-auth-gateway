package io.github.udayhe.edgeauthgateway.filter.prefilter.prefilter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static java.util.Objects.nonNull;

/**
 *
 * @author udayhegde
 */

@Slf4j
public class TenantIdentificationGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String tenantId = exchange.getRequest().getQueryParams().getFirst("tenantId");
        if (nonNull(tenantId)) {
            exchange.getAttributes().put("SPRING_SECURITY_TENANT_ID", tenantId);
        } else {
            exchange.getAttributes().put("SPRING_SECURITY_TENANT_ID", "defaultTenantId");
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
