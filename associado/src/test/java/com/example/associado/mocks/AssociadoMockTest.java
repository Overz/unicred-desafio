package com.example.associado.mocks;

import com.example.associado.models.Associado;

import static com.example.common.constants.AssociadoConstants.PESSOA_FISICA;
import static com.example.common.generators.Nanoid.nanoid;

public abstract class AssociadoMockTest {

	public static final String DOC_1 = "08696286073";
	public static final String DOC_2 = "18763516080";
	public static final String DOC_3 = "09188071073";
	public static final String DOC_4 = "91325814083";

	public static Associado getAssociado() {
		return Associado
			.builder()
			.uuid(nanoid())
			.nome("nome")
			.documento(DOC_1)
			.tipo_pessoa(PESSOA_FISICA)
			.build();
	}
}
