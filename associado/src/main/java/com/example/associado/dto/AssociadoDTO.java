package com.example.associado.dto;

import com.example.associado.models.Associado;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
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

  @NotEmpty
  @NotNull
  @Pattern(message = "O Documento deve ser o CPF/CNPJ sem formatação", regexp = "^\\d{11,14}$")
  private String documento;

  @NotEmpty
  @NotNull
  @Pattern(message = "O tipo da pessoa deve ser 'PF' ou 'PJ'", regexp = "^(PF)|(PJ)$", flags = {Pattern.Flag.CASE_INSENSITIVE})
  private String tipoPessoa;

  @NotEmpty
  @NotNull
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
