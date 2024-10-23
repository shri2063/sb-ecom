package com.ecommerce.sb_ecom.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class UserInfoResponse
{
    private Long id;
    private  String jwtToken;
    private String username;
    private List<String> roles;
}