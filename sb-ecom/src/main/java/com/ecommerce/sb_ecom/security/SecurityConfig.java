package com.ecommerce.sb_ecom.security;

import com.ecommerce.sb_ecom.jwt.AuthEntryPointJwt;
import com.ecommerce.sb_ecom.jwt.AuthTokenFilter;
import com.ecommerce.sb_ecom.model.AppRole;
import com.ecommerce.sb_ecom.model.User;
import com.ecommerce.sb_ecom.model.Role;
import com.ecommerce.sb_ecom.repositories.UserRepository;
import com.ecommerce.sb_ecom.repositories.RolesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                    //.requestMatchers("/api/public/**").permitAll()
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
                                      UserRepository userRepository, PasswordEncoder passwordEncoder)
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

            Set<Role> userRoleSet = Set.of(userRole);
            Set<Role> adminRoleSet = Set.of(adminRole);
            Set<Role> sellerRoleSet = Set.of(sellerRole);
            if (!userRepository.existsByUsername("user1")) {
                User user1 = new User("user1",  passwordEncoder.encode("password1"),"user1@example.com");
                userRepository.save(user1);
            }

            // Update roles for existing users
            userRepository.findByUsername("user1").ifPresent(user -> {
                user.setRoles(userRoleSet);
                userRepository.save(user);
            });

            if (!userRepository.existsByUsername("seller1")) {
                User seller1 = new User("seller1",  passwordEncoder.encode("password2"),"seller1@example.com");
                userRepository.save(seller1);
            }

            if (!userRepository.existsByUsername("admin")) {
                User admin = new User("admin", passwordEncoder.encode("adminPass"),"admin@example.com");
                userRepository.save(admin);
            }
            userRepository.findByUsername("seller1").ifPresent(seller -> {
                seller.setRoles(sellerRoleSet);
                userRepository.save(seller);
            });

            userRepository.findByUsername("admin").ifPresent(admin -> {
                admin.setRoles(adminRoleSet);
                userRepository.save(admin);
            });




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
