package com.example.common.events;

import java.util.Objects;

public class MessageStreaming {

	private String subject;
	private Long time;
	private Integer sequence;
	private Object data;

	public MessageStreaming() {
	}

	public MessageStreaming(String subject, Long time, Integer sequence, Object data) {
		this.subject = subject;
		this.time = time;
		this.sequence = sequence;
		this.data = data;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof MessageStreaming that)) return false;
		return Objects.equals(getSubject(), that.getSubject()) && Objects.equals(getTime(), that.getTime()) && Objects.equals(getSequence(), that.getSequence()) && Objects.equals(getData(), that.getData());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSubject(), getTime(), getSequence(), getData());
	}

	@Override
	public String toString() {
		return "MessageStreaming{" +
			"subject='" + subject + '\'' +
			", time=" + time +
			", sequence=" + sequence +
			", data=" + data +
			'}';
	}
}
