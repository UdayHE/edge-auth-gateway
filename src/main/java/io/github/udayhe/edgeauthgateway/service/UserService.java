package io.github.udayhe.edgeauthgateway.service;

import io.github.udayhe.edgeauthgateway.dao.UserProfile;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

/**
 * @author udayhegde
 */
public interface UserService {


    UserProfile getUser(OidcUser principal, OAuth2AuthorizedClient authorizedClient);
}
