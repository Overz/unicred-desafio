package com.example.common.events;

import com.example.common.mappers.MapperUtils;

public abstract class EventHelpers {

	public static String toMessage(String subject, Object o) {
		return toMessage(subject, -1, o);
	}

	/**
	 * Transforma a mensagem em um modelo padrão
	 *
	 * @param subject  Tipo do evento, utilizado para separação
	 * @param sequence Sequencia da mensagem, caso precise seguir uma ordem
	 * @param o        Objeto enviado
	 * @return Mensagem serializada
	 */
	public static String toMessage(String subject, int sequence, Object o) {
		MessageStreaming<Object> m = new MessageStreaming<>();
		m.setSubject(subject);
		m.setData(o);
		m.setTime(System.currentTimeMillis());

		if (sequence > -1) {
			m.setSequence(sequence);
		}

		return MapperUtils.toJson(m);
	}

	public static <T> MessageStreaming<T> fromMessage(String message) {
		return MapperUtils.fromJson(message, MessageStreaming.class);
	}
}
