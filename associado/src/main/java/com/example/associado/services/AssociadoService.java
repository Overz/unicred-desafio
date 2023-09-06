package com.example.associado.services;

import com.example.common.errors.BadRequestError;
import com.example.common.errors.NotFoundError;
import com.example.associado.models.Associado;
import com.example.associado.repositories.AssociadoRepository;
import com.example.common.validations.CpfCnpj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.common.generators.Nanoid.nanoid;

@Service
public class AssociadoService {

  @Autowired
  private AssociadoRepository repo;

  public Associado criarAssociado(Associado o) {
    if (CpfCnpj.isInvalid(o.getDocumento())) {
      throw new BadRequestError("CPF/CNPJ não aceito");
    }

    long count = repo.count(Example.of(o));
    if (count > 0) {
      throw new BadRequestError("Associado já registrado");
    }

    return repo.save(o);
  }

  public Associado atualizarAssociado(String cpfcnpj, Associado o) {
    if (CpfCnpj.isInvalid(o.getDocumento())) {
      throw new BadRequestError("CPF/CNPJ não aceito!");
    }

    Associado example = Associado.builder().documento(o.getDocumento()).build();

    long count = repo.count(Example.of(example));
    if (count > 0) {
      throw new BadRequestError("CPF/CNPJ já registrado");
    }

    example = Associado.builder().documento(cpfcnpj).build();
    Optional<Associado> data = repo.findBy(Example.of(example), FluentQuery.FetchableFluentQuery::first);

    if (data.isEmpty()) {
      return null;
    }

    Associado associado = data.get();
    associado.setNome(o.getNome());
    associado.setDocumento(o.getDocumento());
    associado.setTipo_pessoa(o.getTipo_pessoa());

    return repo.save(associado);
  }

  public void excluir(String cpfcnpj) {
    // TODO
  }

  public Associado consultarDocumento(String cpfcnpj) {
    Associado example = Associado.builder().documento(cpfcnpj).build();
    Optional<Associado> data = repo.findOne(Example.of(example));

    if (data.isEmpty()) {
      throw new NotFoundError();
    }

    return data.get();
  }

  public List<Associado> listar() {
    return repo.findAll();
  }
}
