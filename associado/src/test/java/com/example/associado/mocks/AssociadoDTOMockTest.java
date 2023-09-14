package com.example.associado.mocks;

import com.example.associado.dto.AssociadoDTO;

import java.util.List;

public abstract class AssociadoDTOMockTest {

  public static AssociadoDTO getAssociadoDTO() {
    return AssociadoMockTest.getAssociado().toDTO();
  }

  public static List<AssociadoDTO> getListAssociadoDTO() {
    return List.of(getAssociadoDTO());
  }
}
