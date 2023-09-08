package com.example.arquivo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class ArquivoComPathDTO implements Serializable {

	@NotEmpty(message = "caminho não deve ser vazio")
	@NotNull(message = "caminho não deve ser nulo")
	private String caminho;
}
