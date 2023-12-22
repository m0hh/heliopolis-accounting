package com.helioplis.accounting.security.jwt.repo;

import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import com.helioplis.accounting.security.jwt.entity.UserHelioplis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserHelioplis,Integer> {
    Optional<UserHelioplis> findByUsername(String username);

}
