package com.vikku.taskplanner.auth.controller;

import com.vikku.taskplanner.auth.model.dtos.response.LoginResponse;
import com.vikku.taskplanner.auth.model.dtos.response.Oauth2AuthorizeResponse;
import com.vikku.taskplanner.auth.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/oauth2")
@RequiredArgsConstructor
public class OAuth2Controller {
    private final OAuth2Service oAuth2Service;

    // Step 1: Redirect user to provider login
    @GetMapping("/authorize/{provider}")
    public ResponseEntity<Oauth2AuthorizeResponse> authorize(@PathVariable String provider,
                                                             @RequestParam String state) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(oAuth2Service.getAuthorizationUrl(provider, state));
    }

    // Step 2: Provider redirects here after user consents
    @GetMapping("/callback/{provider}")
    public ResponseEntity<LoginResponse> callback(@PathVariable String provider,
                                                  @RequestParam("code") String code) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(oAuth2Service.processCallback(provider, code));
    }
}


//http://localhost:8080/api/auth/oauth2/authorize/google
//http://localhost:8080/api/auth/oauth2/authorize/:{provider}