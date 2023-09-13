package com.example.associado.repositories;

import com.example.associado.models.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AssociadoRepository extends JpaRepository<Associado, String> {

	@Modifying
	@Query("delete from Associado where documento = ?1")
	void deletarPorCpfCpnj(String cpfcnpj);

	@Query("from Associado where documento = ?1")
	Optional<Associado> consultarPorCpfCnpj(String cpfcnpj);
}
