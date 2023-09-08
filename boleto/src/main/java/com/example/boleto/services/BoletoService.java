package com.example.boleto.services;

import com.example.boleto.models.Boleto;
import com.example.boleto.repositories.BoletoRepository;
import com.example.common.constants.BoletoConstants;
import com.example.common.errors.BadRequestError;
import com.example.common.errors.NotFoundError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoletoService {

	@Autowired
	private BoletoRepository repo;

	public Boleto criarBoleto(Boleto o) {
		return repo.save(o);
	}

	public void atualizarBoleto(String boletoId, Boleto o) {
		Boleto example = Boleto.builder().uuid(boletoId).build();
		Optional<Boleto> opt = repo.findOne(Example.of(example));

		if (opt.isEmpty()) {
			throw new NotFoundError();
		}

		Boleto boleto = opt.get();
		boleto.setValor(o.getValor());
		boleto.setVencimento(o.getVencimento());
		boleto.setDocumento_pagador(o.getDocumento_pagador());
		boleto.setUuid_associado(o.getUuid_associado());
		boleto.setNome_pagador(o.getNome_pagador());
		boleto.setNome_fantasia_pagador(o.getNome_fantasia_pagador());
		boleto.setSituacao(o.getSituacao());

		repo.save(boleto);
	}

	public void excluirBoletoPorId(String boletoId) {
		repo.deleteById(boletoId);
	}

	public Boleto consultarBoletoPorId(String boletoId) {
		Optional<Boleto> opt = repo.findById(boletoId);

		if (opt.isEmpty()) {
			throw new NotFoundError();
		}

		return opt.get();
	}

	public List<Boleto> listarBoletosPorAssociado(String id) {
		Boleto example = Boleto.builder().uuid_associado(id).build();
		return repo.findAll(Example.of(example));
	}

	public long contarBoletosNaoPagosPorAssociado(String idAssociado) {
		return repo.contarBoletosNaoPagosPorAssociado(idAssociado);
	}

	public List<Boleto> listar() {
		return repo.findAll();
	}

	public void pagarBoleto(String id, String cpfcnpj, BigDecimal valor) {
		Optional<Boleto> opt = repo.consultarBoletoParaPagamento(id, cpfcnpj, valor);

		if (opt.isEmpty()) {
			throw new NotFoundError();
		}

		Boleto boleto = opt.get();
		if (boleto.getSituacao().equalsIgnoreCase(BoletoConstants.PAGO)) {
			throw new BadRequestError("Boleto atualmente pago");
		}

		boleto.setSituacao(BoletoConstants.PAGO);

		repo.save(boleto);
	}
}
