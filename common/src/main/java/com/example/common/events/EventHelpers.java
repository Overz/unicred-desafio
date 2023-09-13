package com.example.common.events;

import com.example.common.mappers.MapperUtils;

public abstract class EventHelpers {

	public static String toMessage(Object o) {
		return toMessage(null, null, o);
	}

	public static String toMessage(Integer sequence, Object o) {
		return toMessage(null, sequence, o);
	}

	public static String toMessage(String subject, Object o) {
		return toMessage(subject, null, o);
	}

	/**
	 * Transforma a mensagem em um modelo padrão
	 *
	 * @param subject  Tipo do evento, utilizado para separação
	 * @param sequence Sequencia da mensagem, caso precise seguir uma ordem
	 * @param o        Objeto enviado
	 * @return Mensagem serializada
	 */
	public static <T> String toMessage(String subject, Integer sequence, T o) {
		MessageStreaming<T> m = new MessageStreaming<>();
		m.setData(o);
		m.setTime(System.currentTimeMillis());

		if (subject != null && !subject.isEmpty()) {
			m.setSubject(subject);
		}

		if (sequence != null) {
			m.setSequence(sequence);
		}

		return MapperUtils.toJson(m);
	}

	public static <T> MessageStreaming<T> fromMessage(String message) {
		return MapperUtils.fromJson(message, MessageStreaming.class);
	}
}
