package io.github.udayhe.edgeauthgateway.service.impl;

import io.github.udayhe.edgeauthgateway.dao.UserProfile;
import io.github.udayhe.edgeauthgateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

/**
 * @author udayhegde
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    @Override
    public UserProfile getUser(OidcUser principal, OAuth2AuthorizedClient authorizedClient) {
        // Assuming 'name' and 'email' are part of the response based on the 'profile' and 'email' scopes
        if (principal == null || principal.getAttributes().isEmpty())
            return UserProfile.builder().info("Not an authenticated user").build();
        return UserProfile.builder()
                .userName(principal.getAttribute("name"))
                .userEmail(principal.getAttribute("email"))
                .accessToken(authorizedClient.getAccessToken().getTokenValue())
                .idTokenHint(principal.getIdToken().getTokenValue())
                .build();
    }
}
