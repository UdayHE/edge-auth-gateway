package io.github.udayhe.edgeauthgateway.dao;

import lombok.*;

/**
 *
 * @author udayhegde
 */

@Data
@Getter
@Setter
@AllArgsConstructor
public class TenantConfig {
    private String registrationId;
    private String domain;
    private String clientId;
    private String clientSecret;
    private String tenant;
}
