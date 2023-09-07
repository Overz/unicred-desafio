package com.example.boleto.dto;

import com.example.boleto.models.Boleto;
import com.example.common.constants.BoletoConstants;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class BoletoDTO implements Serializable {
	private String id;

	@NotNull(message = "Valor não deve ser nulo")
//	@NotEmpty(message = "Valor não deve ser vazio")
	@Digits(integer = 10, fraction = 2, message = "O valor deve ser númerico flutuante de 10,2")
	private BigDecimal valor;

	@NotNull(message = "Vencimento não deve ser nulo")
//	@NotEmpty(message = "Vencimento não deve ser vazio")
	@Future(message = "O vencimento deve ser maior que a data atual")
	private LocalDateTime vencimento;

	@NotNull(message = "Associado não deve ser nulo")
	@NotEmpty(message = "Associado não deve ser vazio")
	private String associado;

	@NotNull(message = "Documento Pagador não deve ser nulo")
	@NotEmpty(message = "Documento Pagador não deve ser vazio")
	@Pattern(message = "Documento Pagador deve ser o CPF/CNPJ sem formatação", regexp = "^\\d{11,14}$")
	private String documentoPagador;

	@NotNull(message = "Nome do Pagador não deve ser nulo")
	@NotEmpty(message = "Nome do Pagador não deve ser vazio")
	private String nomePagador;

	private String nomeFantasiaPagador;

	@NotNull(message = "Situacao não deve ser nulo")
	@NotEmpty(message = "Situacao não deve ser vazio")
	@Pattern(message = "Situacao deve estar inclusoo em: "
		+ BoletoConstants.PENDENTE + "," + BoletoConstants.PAGO + "," + BoletoConstants.CANCELADO,
		regexp = "^" + BoletoConstants.PENDENTE + "|" + BoletoConstants.PAGO + "|" + BoletoConstants.CANCELADO + "$"
	)
	private String situacao;

	public Boleto toEntity() {
		return Boleto
			.builder()
			.uuid(id)
			.valor(valor)
			.vencimento(vencimento)
			.uuid_associado(associado)
			.documento_pagador(documentoPagador)
			.nome_pagador(nomePagador)
			.nome_fantasia_pagador(nomeFantasiaPagador)
			.situacao(situacao)
			.build();
	}
}
