package com.example.associado.mocks;

import com.example.associado.models.Associado;

import java.util.List;

import static com.example.common.constants.AssociadoConstants.PESSOA_FISICA;
import static com.example.common.generators.Nanoid.nanoid;

public abstract class AssociadoMockTest {

  public static final String DOC_1 = "17904033089";
  public static final String DOC_2 = "58124967067";
  public static final String DOC_3 = "50539925098";
  public static final String DOC_4 = "38497387000106";

  public static final String ID_1 = "1";
  public static final String ID_2 = "2";
  public static final String ID_3 = "3";
  public static final String ID_4 = "4";

  public static Associado getAssociado() {
    return Associado
        .builder()
        .uuid(nanoid())
        .nome("nome")
        .documento("08696286073")
        .tipo_pessoa(PESSOA_FISICA)
        .build();
  }

  public static List<Associado> getAssociadoList() {
    return List.of(getAssociado());
  }
}
