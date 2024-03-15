package io.github.udayhe.edgeauthgateway.repository.impl;


import io.github.udayhe.edgeauthgateway.dao.RememberMeToken;
import io.github.udayhe.edgeauthgateway.repository.TokenRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author udayhegde
 */

public class InMemoryTokenRepository implements TokenRepository {
    private final Map<String, RememberMeToken> tokens = new ConcurrentHashMap<>();

    @Override
    public void saveToken(RememberMeToken token) {
        tokens.put(token.getToken(), token);
    }

    @Override
    public RememberMeToken findByToken(String token) {
        return tokens.get(token);
    }

    @Override
    public RememberMeToken findByUsername(String username) {
        return tokens.values().stream()
                .filter(token -> token.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteToken(String token) {
        tokens.remove(token);
    }

    @Override
    public void invalidateUserTokens(String username) {
        var tokensToRemove = tokens.values().stream()
                .filter(token -> token.getUsername().equals(username))
                .map(RememberMeToken::getToken)
                .toList();
        tokensToRemove.forEach(tokens::remove);
    }
}
