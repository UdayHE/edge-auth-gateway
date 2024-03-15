package io.github.udayhe.edgeauthgateway.dao;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * @author udayhegde
 */

@Component
@ConfigurationProperties(prefix = "tenants")
@Data
public class TenantProperties {
    private List<TenantConfig> configs;

}