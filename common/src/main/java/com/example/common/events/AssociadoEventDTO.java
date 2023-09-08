package com.example.common.events;

import java.io.Serializable;
import java.util.Objects;

public class AssociadoEventDTO implements Serializable {

	public AssociadoEventDTO() {
	}

	public AssociadoEventDTO(String uuid, String documento, String tipo_pessoa, String nome) {
		this.uuid = uuid;
		this.documento = documento;
		this.tipo_pessoa = tipo_pessoa;
		this.nome = nome;
	}

	private String uuid;
	private String documento;
	private String tipo_pessoa;
	private String nome;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getTipo_pessoa() {
		return tipo_pessoa;
	}

	public void setTipo_pessoa(String tipo_pessoa) {
		this.tipo_pessoa = tipo_pessoa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AssociadoEventDTO that)) return false;
		return Objects.equals(uuid, that.uuid) && Objects.equals(documento, that.documento) && Objects.equals(tipo_pessoa, that.tipo_pessoa) && Objects.equals(nome, that.nome);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uuid, documento, tipo_pessoa, nome);
	}

	@Override
	public String toString() {
		return "AssociadoEventDTO{" +
			"uuid='" + uuid + '\'' +
			", documento='" + documento + '\'' +
			", tipo_pessoa='" + tipo_pessoa + '\'' +
			", nome='" + nome + '\'' +
			'}';
	}
}
