package com.vikku.authservice.model.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Oauth2AuthorizeResponse {
    private String redirectUrl;
}
