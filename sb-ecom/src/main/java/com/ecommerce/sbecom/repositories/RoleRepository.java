package com.ecommerce.sbecom.repositories;

import com.ecommerce.sbecom.models.AppRole;
import com.ecommerce.sbecom.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole roleName);
}
