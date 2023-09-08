package com.example.common.utils;


public class Sanitizer {

	private Sanitizer() {
	}

	/**
	 * concatena strings em uma posição fixa determinada
	 * até atingir o size correto
	 * <p>
	 * disable: size = -1 || value = null
	 *
	 * @param value         valor
	 * @param concatenation string para concatenação
	 * @param size          tamanho máximo do texto formatado
	 * @param offset        posição onde concatenar
	 * @return value - valor corrigido
	 */
	public static String toFixLength(String value, String concatenation, int size, int offset) {
		if (value == null) {
			value = "";
		}

		if (size <= 0) {
			return value;
		}

		StringBuilder builder = new StringBuilder(value);

		while (builder.length() < size) {
			builder.insert(offset, concatenation);
		}

		return builder.toString();
	}
}
