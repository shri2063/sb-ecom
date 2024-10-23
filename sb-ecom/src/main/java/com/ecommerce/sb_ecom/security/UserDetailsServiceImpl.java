package com.ecommerce.sb_ecom.security;

import com.ecommerce.sb_ecom.model.User;
import com.ecommerce.sb_ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository clientUserRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User clientUser = clientUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("user  not found: {}" + username));

        System.out.println("roles: "+clientUser.getRoles());
        return UserDetailsImpl.build(clientUser);




    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer()
    {
        return (web -> web.ignoring()
                .requestMatchers(
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/swagger-ui.html",
                        "/webjars/**"
                ));
    }
}
