package io.github.udayhe.edgeauthgateway.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;

import static io.github.udayhe.edgeauthgateway.utils.NullSafeUtils.nullSafeGet;
import static java.util.Objects.nonNull;
import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.apache.logging.log4j.util.Strings.isNotBlank;

/**
 *
 * @author udayhegde
 */

@Slf4j
@UtilityClass
public class TenantResolver {

    /**
     * Extracts the tenant ID from the request subdomain.
     *
     * @param exchange the current server web exchange
     * @return the resolved tenant ID or a default value if not found
     */
    public static String resolveTenant(ServerWebExchange exchange) {

        String tenantId = extractTenantFromHost(exchange.getRequest().getURI().getHost());

        if (StringUtils.isEmpty(tenantId)) {
            tenantId = exchange.getRequest().getQueryParams().getFirst("tenantId");
        }

        return StringUtils.hasText(tenantId) ? tenantId : Constants.DEFAULT_OKTA_PROVIDER; // Fallback tenant ID
    }

    /**
     * Extracts the tenant ID from the host string.
     *
     * @param host the host string from the request URI
     * @return the extracted tenant ID
     */
    private static String extractTenantFromHost(String host) {
        int firstDotIndex = host.indexOf('.');
        if (firstDotIndex > -1) {
            return host.substring(0, firstDotIndex);
        }
        return null;
    }

    /**Extracts registrationId from provided URL
     *
     * @param uri
     * @return tenant or empty string
     */
    public static String extractRegistrationIdFromURL(URI uri) {
        try {
            String query = nullSafeGet(uri, URI::getQuery, EMPTY);
            if(isNotBlank(query)) {
                String[] queryParams = query.split("&");
                for (String param : queryParams) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && keyValue[0].equals("tenantId")) {
                        return keyValue[1];
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception in extractTenantFromURL,", e);
        }
        return EMPTY;
    }

    public static String extractTenantFromURL(URI uri) {
        try {
            if(nonNull(uri)) {
                String host = uri.getHost();
               return host.split("\\.")[0];
            }
        } catch (Exception e) {
            log.error("Exception in extractTenantFromURL,", e);
        }
        return EMPTY;
    }
}
