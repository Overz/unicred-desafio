package com.example.associado.mocks;

import com.example.associado.models.Associado;

import java.util.UUID;

//import static com.example.common.constants.AssociadoConstants.PESSOA_FISICA;
//import static com.example.common.generators.Nanoid.nanoid;

public abstract class AssociadoMockTest {

	public static final String DOC_1 = "17904033089";
	public static final String DOC_2 = "58124967067";
	public static final String DOC_3 = "50539925098";
	public static final String DOC_4 = "38497387000106";

	public static Associado getAssociado() {
		return Associado
			.builder()
//			.uuid(nanoid())
			.uuid(UUID.randomUUID().toString())
			.nome("nome")
			.documento("08696286073")
//			.tipo_pessoa(PESSOA_FISICA)
			.tipo_pessoa("PF")
			.build();
	}
}
