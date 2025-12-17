package com.renato.pruebatecnica.seek.prueba_tecnica_seek.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
