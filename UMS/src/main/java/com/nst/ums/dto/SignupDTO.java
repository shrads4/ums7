package com.nst.ums.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class SignupDTO {
    private String username;
    private String password;
    private String id;
    private String roles;
}
