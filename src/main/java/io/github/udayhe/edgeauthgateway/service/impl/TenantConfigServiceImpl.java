package io.github.udayhe.edgeauthgateway.service.impl;

import io.github.udayhe.edgeauthgateway.dao.TenantConfig;
import io.github.udayhe.edgeauthgateway.dao.TenantProperties;
import io.github.udayhe.edgeauthgateway.registration.OktaClientRegistration;
import io.github.udayhe.edgeauthgateway.service.TenantConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author udayhegde
 */

@Service
@Slf4j
@AllArgsConstructor
public class TenantConfigServiceImpl implements TenantConfigService {

    private final TenantProperties tenantProperties;
    private Map<String, ClientRegistration> registrations;

    @Override
    public List<ClientRegistration> getAllTenantConfigs() {
        registrations = tenantProperties.getConfigs().stream()
                .map(config -> OktaClientRegistration.oktaClientRegistration(
                        config.getRegistrationId(),
                        config.getDomain(),
                        config.getClientId(),
                        config.getClientSecret(), config.getTenant())
                )
                .collect(Collectors.toMap(ClientRegistration::getRegistrationId, config -> config));
        return new ArrayList<>(registrations.values());
    }

    @Override
    public ClientRegistration getClientRegistrationByRegistrationId(String registrationId) {
        return this.registrations.get(registrationId);
    }

    @Override
    public Optional<TenantConfig> getTenantProperties(String registrationId) {
        return tenantProperties.getConfigs()
                .stream()
                .filter(config -> config.getRegistrationId().equals(registrationId))
                .findFirst();
    }
}
