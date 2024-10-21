package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.repository;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(Long id);

    void deleteById(Long id);

    boolean existsByUsername(String username);


}
