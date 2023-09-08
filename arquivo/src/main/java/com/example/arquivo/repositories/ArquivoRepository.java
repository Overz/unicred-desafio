package com.example.arquivo.repositories;

import com.example.arquivo.models.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArquivoRepository extends JpaRepository<Arquivo, String> {
}
