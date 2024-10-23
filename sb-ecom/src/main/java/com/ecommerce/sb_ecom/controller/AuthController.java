package com.ecommerce.sb_ecom.controller;

import com.ecommerce.sb_ecom.jwt.JwtUtils;
import com.ecommerce.sb_ecom.jwt.LoginRequest;
import com.ecommerce.sb_ecom.jwt.SignupRequest;
import com.ecommerce.sb_ecom.jwt.UserInfoResponse;
import com.ecommerce.sb_ecom.model.AppRole;
import com.ecommerce.sb_ecom.model.ClientUser;
import com.ecommerce.sb_ecom.model.Role;
import com.ecommerce.sb_ecom.repositories.ClientUserRepository;
import com.ecommerce.sb_ecom.repositories.RolesRepository;
import com.ecommerce.sb_ecom.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController
{


    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    ClientUserRepository clientUserRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid LoginRequest request)
    {
        try
        {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            String token = jwtUtils.generateTokenFromUsername(userDetails);
            List<String> roles = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            UserInfoResponse response = new UserInfoResponse(userDetails.getId(),token,  userDetails.getUsername(),roles);

            return new ResponseEntity<>(response,HttpStatus.OK);

        }
        catch(Exception ex)
        {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        }


    }

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@Valid SignupRequest request)
    {
        if(clientUserRepository.existsByUsername(request.getUsername()))
        {
            return ResponseEntity.badRequest().body("UserName already exists" );
        }

        if(clientUserRepository.existsByEmail(request.getEmail()))
        {
            return ResponseEntity.badRequest().body("email already exists" );
        }
        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null)
        {
            Role userRole = rolesRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: role not found"));
            roles.add(userRole);
        }
        else
        {
          strRoles.forEach(role ->
          {
              switch (role)
              {
                  case "admin":
                      Role adminRole = rolesRepository.findByRoleName(AppRole.ROLE_ADMIN)
                              .orElseThrow(() -> new RuntimeException("Error: role not found"));
                      roles.add(adminRole);
                      break;
                  case "seller":
                      Role sellerRole = rolesRepository.findByRoleName(AppRole.ROLE_SELLER)
                              .orElseThrow(() -> new RuntimeException("Error: role not found"));
                      roles.add(sellerRole);
                      break;
                  default:
                      Role userRole = rolesRepository.findByRoleName(AppRole.ROLE_USER)
                              .orElseThrow(() -> new RuntimeException("Error: role not found"));
                      roles.add(userRole);
              }
          });
        }

        ClientUser newUser = new ClientUser(request.getEmail(), passwordEncoder.encode(request.getPassword()), request.getUsername(), roles);
        clientUserRepository.save(newUser);
        return ResponseEntity.ok("Success");

    }

}
