package io.github.udayhe.edgeauthgateway.registration;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

/**
 *
 * @author udayhegde
 */

public class GoogleClientRegistration {

    public static ClientRegistration googleClientRegistration(String tenantId, String serverName) {
        return ClientRegistration.withRegistrationId("google" + tenantId)
                .clientId("google-client-id-for-" + tenantId)
                .clientSecret("google-client-secret-for-" + tenantId)
                .scope("openid", "profile", "email")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("{baseUrl}/{action}/oauth2/code/{registrationId}")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://oauth2.googleapis.com/token")
                .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google " + tenantId)
                .build();
    }


}
