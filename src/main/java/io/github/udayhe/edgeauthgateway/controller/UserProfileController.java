package io.github.udayhe.edgeauthgateway.controller;


import io.github.udayhe.edgeauthgateway.dao.UserProfile;
import io.github.udayhe.edgeauthgateway.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author udayhegde
 */

@RestController
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/user/profile")
    public ResponseEntity<UserProfile> userProfile(@AuthenticationPrincipal OidcUser principal,
                                                   @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        return ResponseEntity.ok(userService.getUser(principal, authorizedClient));
    }
}
