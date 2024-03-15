package io.github.udayhe.edgeauthgateway.repository;


import io.github.udayhe.edgeauthgateway.dao.RememberMeToken;

/**
 *
 * @author udayhegde
 */

public interface TokenRepository {
    void saveToken(RememberMeToken token);

    RememberMeToken findByToken(String token);

    RememberMeToken findByUsername(String username);

    void deleteToken(String token);

    void invalidateUserTokens(String username);
}