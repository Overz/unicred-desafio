package com.example.boleto.models;

import com.example.boleto.dto.BoletoDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.common.generators.Nanoid.nanoid;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = Boleto.TABLE)
public class Boleto implements Serializable {
	public static final String TABLE = "boleto";

	@Id
	@Column(name = Fields.uuid, nullable = false)
	private String uuid;

	@Column(name = Fields.valor, nullable = false, precision = 2)
	private BigDecimal valor;

	@Column(name = Fields.vencimento, nullable = false)
	private LocalDateTime vencimento;

	@Column(name = Fields.uuid_associado, nullable = false)
	private String uuid_associado;

	@Column(name = Fields.documento_pagador, nullable = false, length = 14)
	private String documento_pagador;

	@Column(name = Fields.nome_pagador, nullable = false, length = 50)
	private String nome_pagador;

	@Column(name = Fields.nome_fantasia_pagador, length = 50)
	private String nome_fantasia_pagador;

	@Column(name = Fields.situacao, nullable = false, length = -1)
	private String situacao;

	@PrePersist
	public void createId() {
		this.uuid = nanoid();
	}

	public BoletoDTO toDTO() {
		return BoletoDTO
			.builder()
			.id(uuid)
			.valor(valor)
			.vencimento(vencimento)
			.associado(uuid_associado)
			.documentoPagador(documento_pagador)
			.nomePagador(nome_pagador)
			.nomeFantasiaPagador(nome_fantasia_pagador)
			.situacao(situacao)
			.build();
	}
}
