package io.github.udayhe.edgeauthgateway.configuration;

import io.github.udayhe.edgeauthgateway.service.impl.TenantConfigServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.*;

/**
 *
 * @author udayhegde
 */


@Configuration
@Slf4j
@AllArgsConstructor
public class EdgeConfig {

    private final TenantConfigServiceImpl configService;
    @Bean
    public ReactiveClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryReactiveClientRegistrationRepository(configService.getAllTenantConfigs());
    }


}
