package com.example.arquivo.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
@Entity
@Table
public class Arquivo implements Serializable {

	@Id
	@Column(name = Fields.id, nullable = false)
	private String id;

	@Column(name = Fields.status, nullable = false)
	private String status;

	@Column(name = Fields.valor, nullable = false)
	private String valor;

	@Column(name = Fields.documento, nullable = false)
	private String documento;

	@Column(name = Fields.dtProcessamento, nullable = false)
	private Long dtProcessamento;

	@Transient
	private Boolean isCpf;
}
