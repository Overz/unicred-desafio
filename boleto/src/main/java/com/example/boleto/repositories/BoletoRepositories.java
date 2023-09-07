package com.example.boleto.repositories;

import com.example.boleto.models.Boleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;

public interface BoletoRepositories extends JpaRepository<Boleto, String> {

	@Query("select count(*) from Boleto where situacao != 'PAGO' and uuid_associado = ?1")
	long contarBoletosNaoPagosPorAssociado(String id);

	@Query("from Boleto where uuid = ?1 and documento_pagador = ?2 and valor = ?3")
	Optional<Boleto> consultarBoletoParaPagamento(String uuid, String documento, BigDecimal valor);
}
