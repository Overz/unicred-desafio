package com.example.common.events;

import java.io.Serializable;
import java.util.Objects;

public class ArquivoEventDTO implements Serializable {

	public ArquivoEventDTO() {
	}

	public ArquivoEventDTO(String id, String status, String valor, String documento, Long dtProcessamento, Boolean isCpf) {
		this.id = id;
		this.status = status;
		this.valor = valor;
		this.documento = documento;
		this.dtProcessamento = dtProcessamento;
		this.isCpf = isCpf;
	}

	private String id;
	private String status;
	private String valor;
	private String documento;
	private Long dtProcessamento;
	private Boolean isCpf;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Long getDtProcessamento() {
		return dtProcessamento;
	}

	public void setDtProcessamento(Long dtProcessamento) {
		this.dtProcessamento = dtProcessamento;
	}

	public Boolean getCpf() {
		return isCpf;
	}

	public void setCpf(Boolean cpf) {
		isCpf = cpf;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ArquivoEventDTO that)) return false;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getStatus(), that.getStatus()) && Objects.equals(getValor(), that.getValor()) && Objects.equals(getDocumento(), that.getDocumento()) && Objects.equals(getDtProcessamento(), that.getDtProcessamento()) && Objects.equals(isCpf, that.isCpf);
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getStatus(), getValor(), getDocumento(), getDtProcessamento(), isCpf);
	}

	@Override
	public String toString() {
		return "ArquivoEventDTO{" +
			"id='" + id + '\'' +
			", status='" + status + '\'' +
			", valor='" + valor + '\'' +
			", documento='" + documento + '\'' +
			", dtProcessamento=" + dtProcessamento +
			", isCpf=" + isCpf +
			'}';
	}
}
