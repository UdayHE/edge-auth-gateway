package io.github.udayhe.edgeauthgateway.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

/**
 *
 * @author udayhegde
 */
@AllArgsConstructor
@Data
@Builder
public class RememberMeToken {
    private String token;
    private String username;
    private Instant expiryDate;
}