package com.vikku.authservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vikku.authservice.model.dtos.response.LoginResponse;
import com.vikku.authservice.model.dtos.response.Oauth2AuthorizeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final JwtService jwtService;
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    // redirect URI must exactly match what's configured in Google console
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    @Value("${spring.security.oauth2.client.provider.google.authorization-uri}")
    private String googleAuthUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String googleTokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String googleUserInfoUri;

    private static final String GOOGLE = "google";

    public Oauth2AuthorizeResponse getAuthorizationUrl(String provider, String state) {
        if (!GOOGLE.equalsIgnoreCase(provider)) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        // include 'state' (anti-forgery) and optionally 'prompt'/'access_type' for refresh tokens
        String url = UriComponentsBuilder
                .fromUriString(googleAuthUri)
                .queryParam("client_id", googleClientId)
                .queryParam("redirect_uri", googleRedirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .queryParam("state", state)
                .build()
                .toUriString();

        return new Oauth2AuthorizeResponse(url);
    }

    /**
     * Exchange the authorization code for provider tokens, fetch userinfo,
     * create/fetch local user, and return your own JWT + refresh token.
     *
     * NOTE: This method assumes AuthService.registerOrFetchOAuthUser returns a domain user
     * object containing at least getId(), getEmail(), getName().
     */
    public LoginResponse processCallback(String provider, String code, String returnedState, String expectedState) {
        if (!GOOGLE.equalsIgnoreCase(provider)) {
            throw new IllegalArgumentException("Unsupported provider: " + provider);
        }

        // Validate state if you used one
        if (expectedState != null && !expectedState.equals(returnedState)) {
            throw new IllegalStateException("Invalid state parameter");
        }

        RestTemplate rest = new RestTemplate();

        // 1) Exchange code for tokens (application/x-www-form-urlencoded)
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> tokenBody = new LinkedMultiValueMap<>();
        tokenBody.add("code", code);
        tokenBody.add("client_id", googleClientId);
        tokenBody.add("client_secret", googleClientSecret);
        tokenBody.add("redirect_uri", googleRedirectUri);
        tokenBody.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(tokenBody, tokenHeaders);
        ResponseEntity<String> tokenResponse = rest.postForEntity(googleTokenUri, tokenRequest, String.class);

        if (!tokenResponse.getStatusCode().is2xxSuccessful() || tokenResponse.getBody() == null) {
            throw new RuntimeException("Failed to exchange code for tokens from Google");
        }

        JsonNode tokenJson;
        try {
            tokenJson = objectMapper.readTree(tokenResponse.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse token response", e);
        }

        // Access token and optionally id_token
        String providerAccessToken = Optional.ofNullable(tokenJson.get("access_token"))
                .map(JsonNode::asText)
                .orElseThrow(() -> new RuntimeException("Missing access_token from provider"));

        String idToken = Optional.ofNullable(tokenJson.get("id_token"))
                .map(JsonNode::asText)
                .orElse(null);

        // 2) Fetch userinfo from provider using access_token
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(providerAccessToken);
        HttpEntity<Void> userReq = new HttpEntity<>(userHeaders);

        ResponseEntity<String> userResp = rest.exchange(googleUserInfoUri, HttpMethod.GET, userReq, String.class);
        if (!userResp.getStatusCode().is2xxSuccessful() || userResp.getBody() == null) {
            throw new RuntimeException("Failed to fetch user info from Google");
        }

        JsonNode userInfo;
        try {
            userInfo = objectMapper.readTree(userResp.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing user info", e);
        }

        String email = Optional.ofNullable(userInfo.get("email")).map(JsonNode::asText)
                .orElseThrow(() -> new RuntimeException("Provider did not return email"));
        boolean emailVerified = Optional.ofNullable(userInfo.get("email_verified")).map(JsonNode::asBoolean).orElse(false);
        String name = Optional.ofNullable(userInfo.get("name")).map(JsonNode::asText).orElse(email);

        // production: enforce emailVerified if required
        if (!emailVerified) {
            // you might still allow, but consider requiring verification
            // throw new RuntimeException("Email not verified by provider");
        }

        // 3) Register or fetch a local user record
        CustomUserDetails user = authService.registerOrFetchOAuthUser(email, name, provider);

        // 4) Generate your own JWT access token (short-lived) and refresh token (longer)
        String accessToken = jwtService.generateJwtToken(user); // assumed method
//        long expiresInSeconds = jwtService.getExpirationDateFromJwtToken(accessToken); // assumed helper
//        String refreshToken = refreshTokenService.createRefreshTokenForUser(user.getId()); // assumed method

        // Optionally record metadata: provider, providerAccessToken, idToken (careful with storing)
        // Do not store provider access token unless you need it, and store encrypted if you do.

        return new LoginResponse(
                accessToken,
                "Bearer",
                11,
                "refreshToken",
                "OAuth2 callback successful"
        );
    }

    // Convenience overload when you don't use state validation:
    public LoginResponse processCallback(String provider, String code) {
        return processCallback(provider, code, null, null);
    }
}
