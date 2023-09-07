package com.example.boleto.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class PagarBoletoDTO implements Serializable {

//	@NotNull(message = "Id não deve ser nulo")
//	@NotEmpty(message = "Id não deve ser vazio")
//	private String id;

	@NotNull(message = "Documento não deve ser nulo")
	@NotEmpty(message = "Documento não deve ser vazio")
	@Pattern(message = "Documento deve ser o CPF/CNPJ sem formatação", regexp = "^\\d{11,14}$")
	private String documento;

	@NotNull(message = "Valor não deve ser nulo")
	@Digits(integer = 10, fraction = 2, message = "O valor deve ser númerico flutuante de 10,2")
	private BigDecimal valor;
}
