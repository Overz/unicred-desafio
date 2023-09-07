package com.example.associado.dto;

import com.example.associado.models.Associado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class AssociadoDTO implements Serializable {
	private String id;

	@NotEmpty(message = "Documento não deve ser vazio")
	@NotNull(message = "Documento não deve ser nulo")
	@Pattern(message = "O Documento deve ser o CPF/CNPJ sem formatação", regexp = "^\\d{11,14}$")
	private String documento;

	@NotEmpty(message = "Tipo Pessoa não deve ser vazio")
	@NotNull(message = "Tipo Pessoa não deve ser nulo")
	@Pattern(message = "O tipo da pessoa deve ser 'PF' ou 'PJ'", regexp = "^(PF)|(PJ)$", flags = {Pattern.Flag.CASE_INSENSITIVE})
	private String tipoPessoa;

	@NotEmpty(message = "Nome não deve ser vazio")
	@NotNull(message = "Nome não deve ser nulo")
	@Pattern(message = "Limite de Caracteres do nome atingido", regexp = "^.{1,50}$")
	private String nome;

	public Associado toEntity() {
		return Associado
			.builder()
			.uuid(this.id)
			.nome(this.nome)
			.documento(this.documento)
			.tipo_pessoa(this.tipoPessoa)
			.build();
	}
}
