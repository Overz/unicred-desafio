package com.example.associado.repositories;

import com.example.associado.models.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AssociadoRepository extends JpaRepository<Associado, String> {

	@Modifying
	@Query("delete from Associado where documento = ?1")
	void deletarPorCpfCpnj(String cpfcnpj);
}
