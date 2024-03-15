package io.github.udayhe.edgeauthgateway.registration;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

/**
 *
 * @author udayhegde
 */

public class OktaClientRegistration {
    public static ClientRegistration oktaClientRegistration(String registrationId, String serverName, String clientId, String clientSecret, String tenant) {
        return ClientRegistration.withRegistrationId(registrationId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .scope("openid", "profile", "email")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .authorizationUri(serverName + "/oauth2/default/v1/authorize")
                .tokenUri(serverName + "/oauth2/default/v1/token")
                .userInfoUri(serverName + "/oauth2/default/v1/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(serverName + "/oauth2/default/v1/keys")
                .clientName(tenant)
                .build();
    }
}
