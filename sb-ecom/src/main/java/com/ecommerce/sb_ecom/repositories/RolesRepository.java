package com.ecommerce.sb_ecom.repositories;

import com.ecommerce.sb_ecom.model.AppRole;
import com.ecommerce.sb_ecom.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface RolesRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByRoleName(AppRole roleName);
}
