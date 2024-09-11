package com.shubham.security.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shubham.security.entity.Users;

@Repository
public interface UserReposiotory extends CrudRepository<Users, Long> {

    Optional<Users> findByEmail(String email);

}
