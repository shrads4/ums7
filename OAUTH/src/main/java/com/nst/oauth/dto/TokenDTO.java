package com.nst.oauth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO {
    private String userId;
    private String accessToken;
    private String refreshToken;
}
