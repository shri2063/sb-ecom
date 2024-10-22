package com.ecommerce.sb_ecom.repositories;

import com.ecommerce.sb_ecom.model.ClientUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public interface ClientUserRepository extends JpaRepository<ClientUser, Long>
{
    Optional<ClientUser> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
