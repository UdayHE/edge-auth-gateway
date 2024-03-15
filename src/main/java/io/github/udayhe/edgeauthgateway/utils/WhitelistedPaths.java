package io.github.udayhe.edgeauthgateway.utils;

import java.util.Set;

/**
 *
 * @author udayhegde
 */
public class WhitelistedPaths {
    public static final Set<String> PUBLIC_PATHS = Set.of("/", "/home", "/public/**", "/app/**", "/actuator/**");
}
