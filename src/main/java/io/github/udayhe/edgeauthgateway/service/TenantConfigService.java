package io.github.udayhe.edgeauthgateway.service;

import io.github.udayhe.edgeauthgateway.dao.TenantConfig;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.List;
import java.util.Optional;

/**
 * @author udayhegde
 */
public interface TenantConfigService {

    List<ClientRegistration> getAllTenantConfigs();

    ClientRegistration getClientRegistrationByRegistrationId(String registrationId);

    Optional<TenantConfig> getTenantProperties(String registrationId);
}
