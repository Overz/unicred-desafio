package com.example.associado.repositories;

import com.example.associado.models.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AssociadoRepository extends JpaRepository<Associado, String> {
}
