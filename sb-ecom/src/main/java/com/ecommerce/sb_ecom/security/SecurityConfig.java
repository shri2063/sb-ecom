package com.ecommerce.sb_ecom.security;

import com.ecommerce.sb_ecom.jwt.AuthEntryPointJwt;
import com.ecommerce.sb_ecom.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig
{

    @Autowired
    DataSource dataSource;
    @Autowired
    private AuthEntryPointJwt unAuthorizedHandler;
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter()
    {
        return new AuthTokenFilter();
    }
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/signin").permitAll()
                    .anyRequest().authenticated();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(unAuthorizedHandler) );

        //http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public UserDetailsService userDetailsService()
    {

        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        return userDetailsManager;
    }

    @Bean
    public CommandLineRunner initData(UserDetailsService userDetailsService)
    {
        return args -> {
            JdbcUserDetailsManager userDetailsManager = (JdbcUserDetailsManager) userDetailsService;
            UserDetails user1 = User.withUsername("user")
                    .password(passwordEncoder().encode("password"))
                    .roles("USER")
                    .build();
            UserDetails admin = User.withUsername("admin")
                    .password(passwordEncoder().encode("password"))
                    .roles("ADMIN")
                    .build();
            userDetailsManager.createUser(user1);
            userDetailsManager.createUser(admin);

        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
