package com.ecommerce.sb_ecom.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SignupRequest
{
    @NotBlank
    private String username;
    @NotBlank
    @Size(min = 3, message = "Size less than 3 characters")
    private String password;
    @NotBlank
    private String email;

    private Set<String> roles;
}
