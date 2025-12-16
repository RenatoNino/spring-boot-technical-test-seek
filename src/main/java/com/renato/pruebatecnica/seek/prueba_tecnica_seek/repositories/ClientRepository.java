package com.renato.pruebatecnica.seek.prueba_tecnica_seek.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.renato.pruebatecnica.seek.prueba_tecnica_seek.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}