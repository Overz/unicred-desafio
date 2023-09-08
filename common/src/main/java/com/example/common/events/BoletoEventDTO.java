package com.example.common.events;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class BoletoEventDTO implements Serializable {

	private String uuid;
	private BigDecimal valor;
	private LocalDateTime vencimento;
	private String uuid_associado;
	private String documento_pagador;
	private String nome_pagador;
	private String nome_fantasia_pagador;
	private String situacao;

	public BoletoEventDTO() {
	}

	public BoletoEventDTO(String uuid, BigDecimal valor, LocalDateTime vencimento, String uuid_associado, String documento_pagador, String nome_pagador, String nome_fantasia_pagador, String situacao) {
		this.uuid = uuid;
		this.valor = valor;
		this.vencimento = vencimento;
		this.uuid_associado = uuid_associado;
		this.documento_pagador = documento_pagador;
		this.nome_pagador = nome_pagador;
		this.nome_fantasia_pagador = nome_fantasia_pagador;
		this.situacao = situacao;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public LocalDateTime getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDateTime vencimento) {
		this.vencimento = vencimento;
	}

	public String getUuid_associado() {
		return uuid_associado;
	}

	public void setUuid_associado(String uuid_associado) {
		this.uuid_associado = uuid_associado;
	}

	public String getDocumento_pagador() {
		return documento_pagador;
	}

	public void setDocumento_pagador(String documento_pagador) {
		this.documento_pagador = documento_pagador;
	}

	public String getNome_pagador() {
		return nome_pagador;
	}

	public void setNome_pagador(String nome_pagador) {
		this.nome_pagador = nome_pagador;
	}

	public String getNome_fantasia_pagador() {
		return nome_fantasia_pagador;
	}

	public void setNome_fantasia_pagador(String nome_fantasia_pagador) {
		this.nome_fantasia_pagador = nome_fantasia_pagador;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BoletoEventDTO that)) return false;
		return Objects.equals(getUuid(), that.getUuid()) && Objects.equals(getValor(), that.getValor()) && Objects.equals(getVencimento(), that.getVencimento()) && Objects.equals(getUuid_associado(), that.getUuid_associado()) && Objects.equals(getDocumento_pagador(), that.getDocumento_pagador()) && Objects.equals(getNome_pagador(), that.getNome_pagador()) && Objects.equals(getNome_fantasia_pagador(), that.getNome_fantasia_pagador()) && Objects.equals(getSituacao(), that.getSituacao());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUuid(), getValor(), getVencimento(), getUuid_associado(), getDocumento_pagador(), getNome_pagador(), getNome_fantasia_pagador(), getSituacao());
	}

	@Override
	public String toString() {
		return "BoletoEventDTO{" +
			"uuid='" + uuid + '\'' +
			", valor=" + valor +
			", vencimento=" + vencimento +
			", uuid_associado='" + uuid_associado + '\'' +
			", documento_pagador='" + documento_pagador + '\'' +
			", nome_pagador='" + nome_pagador + '\'' +
			", nome_fantasia_pagador='" + nome_fantasia_pagador + '\'' +
			", situacao='" + situacao + '\'' +
			'}';
	}
}
