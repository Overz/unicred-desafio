package com.example.associado.models;

import com.example.associado.dto.AssociadoDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

import static com.example.common.generators.Nanoid.nanoid;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table(name = Associado.TABLE)
public class Associado implements Serializable {
  public static final String TABLE = "associado";

  @Id
  @Column(name = Fields.uuid, nullable = false)
  private String uuid;
  @Column(name = Fields.documento, nullable = false, length = 14)
  private String documento;
  @Column(name = Fields.tipo_pessoa, nullable = false, length = 2)
  private String tipo_pessoa;
  @Column(name = Fields.nome, nullable = false, length = 50)
  private String nome;

  @PrePersist
  private void createId() {
    this.uuid = nanoid();
  }

  public AssociadoDTO toDTO() {
    return AssociadoDTO
        .builder()
        .id(this.uuid)
        .nome(this.nome)
        .documento(this.documento)
        .tipoPessoa(this.tipo_pessoa)
        .build();
  }
}
