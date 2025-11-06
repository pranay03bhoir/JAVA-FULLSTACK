package com.ecommerce.sbecom.repositories;

import com.ecommerce.sbecom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserName(String userName);

    Boolean existsByEmail(String email);

}

