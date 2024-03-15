package io.github.udayhe.edgeauthgateway.repository.impl;

import io.github.udayhe.edgeauthgateway.service.TenantConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 *
 * @author udayhegde
 */
@Component
@Slf4j
public class TenantAwareClientRegistrationRepository implements ReactiveClientRegistrationRepository {

    private final TenantConfigService configService;

    public TenantAwareClientRegistrationRepository(TenantConfigService configService) {
        this.configService = configService;
    }

    @Override
    public Mono<ClientRegistration> findByRegistrationId(String registrationId) {
        return Mono.defer(() -> {
            ClientRegistration clientRegistration = configService.getClientRegistrationByRegistrationId(registrationId);
            if (clientRegistration != null) {
                return Mono.just(clientRegistration);
            }
            return Mono.empty();
        });
    }
}
