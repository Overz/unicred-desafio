package com.example.associado.controllers;

import com.example.associado.dto.AssociadoDTO;
import com.example.associado.models.Associado;
import com.example.associado.services.AssociadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/associado")
public class AssociadoController {

  @Autowired
  private AssociadoService service;

//  @Autowired
//  private AssociadoEventProducer producer;

  @PostMapping
  public ResponseEntity<AssociadoDTO> criarAssociado(@RequestBody @Valid AssociadoDTO dto) {
    Associado associado = service.criarAssociado(dto.toEntity());
    // producer.sendMessage(Events.Associado.CRIADO, MapperUtils.toJson(associado));
    return ResponseEntity.status(HttpStatus.CREATED).body(associado.toDTO());
  }

  @PutMapping("/{cpfcnpj}")
  public ResponseEntity<AssociadoDTO> atualizarAssociado(
      @PathVariable("cpfcnpj") String cpfcnpj,
      @RequestBody @Valid AssociadoDTO dto
  ) {
    Associado associado = service.atualizarAssociado(cpfcnpj, dto.toEntity());
    // producer.sendMessage(Events.Associado.ATUALIZADO, MapperUtils.toJson(associado));
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{cpfcnpj}")
  public ResponseEntity<Object> excluir(@PathVariable("cpfcnpj") String cpfcnpj) {
    // service.excluir(cpfcnpj);
    // producer.sendMessage(Events.Associado.EXCLUIR, cpfcnpj);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{cpfcnpj}")
  public ResponseEntity<AssociadoDTO> consultarDocumento(@PathVariable("cpfcnpj") String cpfcnpj) {
    Associado a = service.consultarDocumento(cpfcnpj);
    return ResponseEntity.ok(a.toDTO());
  }

  @GetMapping
  public List<AssociadoDTO> listar() {
    List<Associado> list = service.listar();
    return list.stream().map(Associado::toDTO).toList();
  }
}
