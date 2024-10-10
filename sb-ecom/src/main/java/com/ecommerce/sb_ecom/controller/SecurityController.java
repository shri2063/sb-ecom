package com.ecommerce.sb_ecom.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController
{
    @GetMapping("/hello")
    public String hello()
    {
        return "hello";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminEndPoint()
    {
        return "Hello";
    }
}
