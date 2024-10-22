package com.ecommerce.sb_ecom.jwt;

import lombok.Data;

@Data
public class LoginRequest
{
    private String username;
    private String password;
}
