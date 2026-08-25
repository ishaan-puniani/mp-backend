package com.mp.be.models.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordModel {


    String password;
    String token;
}
