package com.ecommerce.sb_ecom.security;

import com.ecommerce.sb_ecom.jwt.AuthEntryPointJwt;
import com.ecommerce.sb_ecom.jwt.AuthTokenFilter;
import com.ecommerce.sb_ecom.model.AppRole;
import com.ecommerce.sb_ecom.model.ClientUser;
import com.ecommerce.sb_ecom.model.Role;
import com.ecommerce.sb_ecom.repositories.ClientUserRepository;
import com.ecommerce.sb_ecom.repositories.RolesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
import java.io.Serializable;
import java.util.Set;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity

public class SecurityConfig implements Serializable
{

    @Autowired
    DataSource dataSource;
    @Autowired
    private AuthEntryPointJwt unAuthorizedHandler;
    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    @PersistenceContext


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider()
    {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsServiceImpl);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> {
            requests.requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/api/admin/**").permitAll()
                    .requestMatchers("/api/public/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/api/test/**").permitAll()
                    .requestMatchers("/images/**").permitAll()
                    .anyRequest().authenticated();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.exceptionHandling(ex -> ex.authenticationEntryPoint(unAuthorizedHandler) );

        //http.formLogin(Customizer.withDefaults());
        //http.httpBasic(Customizer.withDefaults());
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.csrf(AbstractHttpConfigurer::disable);
        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter()
    {
        return new AuthTokenFilter();
    }


    @Bean
    @Transactional
    public CommandLineRunner initData(RolesRepository rolesRepository,
                                      ClientUserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        return args -> {
            Role userRole = rolesRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() ->
                    {
                        Role newUserRole = new Role(AppRole.ROLE_USER);
                         return rolesRepository.save(newUserRole);

                    });
            Role adminRole = rolesRepository.findByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() ->
                    {
                        Role  newAdminRole = new Role(AppRole.ROLE_ADMIN);
                         rolesRepository.save(newAdminRole);
                        return rolesRepository.findByRoleName(AppRole.ROLE_ADMIN).get();
                    });
            Role sellerRole = rolesRepository.findByRoleName(AppRole.ROLE_SELLER)
                    .orElseGet(() ->
                    {
                        Role  newSellerRole = new Role(AppRole.ROLE_SELLER);
                        rolesRepository.save(newSellerRole);
                        return rolesRepository.findByRoleName(AppRole.ROLE_SELLER).get();
                    });

            //userRole = entityManager.merge(userRole);
            //adminRole = entityManager.merge(adminRole);
            //sellerRole = entityManager.merge(sellerRole);
            userRole = rolesRepository.findByRoleName(AppRole.ROLE_USER).get();
            Set<Role> userRoleSet = Set.of(userRole);
            Set<Role> adminRoleSet = Set.of(adminRole);
            Set<Role> sellerRoleSet = Set.of(sellerRole);

            ClientUser user = new ClientUser("user@user","user", "user", userRoleSet);
            ClientUser admin = new ClientUser("admin@admin","admin", "admin");
            ClientUser seller = new ClientUser("seller@seller","seller", "seller");
            userRepository.save(user);
            userRepository.save(admin);
            userRepository.save(seller);


        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
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

     */

}
