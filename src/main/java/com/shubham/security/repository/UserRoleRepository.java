package com.shubham.security.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shubham.security.entity.UserRoles;

@Repository
public interface UserRoleRepository extends CrudRepository<UserRoles, Long> {

    Optional<UserRoles> findByRoleName(String role);
}
