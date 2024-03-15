package io.github.udayhe.edgeauthgateway.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author udayhegde
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {

    private String userName;
    private String userEmail;
    private String accessToken;
    private String idTokenHint;
    private String info;

}
