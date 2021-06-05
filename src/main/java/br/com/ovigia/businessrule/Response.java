package br.com.ovigia.businessrule;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Response<V> {
	private final ResponseStatus status;
	private final V value;
	private List<String> mensagens;

	private Response() {
		status = ResponseStatus.NO_RESULT;
		value = null;
		mensagens = null;
	}

	private Response(ResponseStatus status, V value, String mensagem) {
		this.status = status;
		this.value = value;
		addMensagem(mensagem);
	}

	private Response(ResponseStatus status, V value, List<String> mensagens) {
		this.status = status;
		this.value = value;
		this.mensagens = mensagens;
	}

	private Response(ResponseStatus status, V value) {
		this.status = status;
		this.value = value;
		this.mensagens = null;
	}

	private Response(ResponseStatus status, List<String> mensagens) {
		this.status = status;
		this.value = null;
		this.mensagens = mensagens;
	}

	private Response(V value) {
		this.status = ResponseStatus.OK;
		this.value = value;
		this.mensagens = null;
	}

	public void addMensagem(String mensagem) {
		if (mensagens == null) {
			mensagens = new ArrayList<>();
		}
		mensagens.add(mensagem);
	}

	@JsonIgnore
	public boolean isBadRequest() {
		return ResponseStatus.BAD_REQUEST == status;
	}

	@JsonIgnore
	public boolean isOk() {
		return ResponseStatus.OK == status;
	}

	@JsonIgnore
	public boolean isNoResult() {
		return ResponseStatus.NO_RESULT == status;
	}

	@JsonIgnore
	public boolean isServerError() {
		return ResponseStatus.INTERNAL_SERVER_ERROR == status;
	}

	@JsonIgnore
	public boolean isUnprocessable() {
		return ResponseStatus.UNPROCESSABLE_ENTITY == status;
	}

	public List<String> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public V getValue() {
		return value;
	}

	public <T> Response<T> clone(T t) {
		return new Response<>(status, t, mensagens);
	}

	public static <T> Response<T> ok(T value) {
		return new Response<>(value);
	}

	public static <T> Response<T> nonResult() {
		return new Response<>();
	}

	public static <T> Response<T> error(T value, String mensagem) {
		return new Response<>(ResponseStatus.INTERNAL_SERVER_ERROR, value, mensagem);
	}

	public static <T> Response<T> unprocessable(T value, String mensagem) {
		return new Response<>(ResponseStatus.UNPROCESSABLE_ENTITY, value, mensagem);
	}
}

enum ResponseStatus {
	OK, NO_RESULT, BAD_REQUEST, INTERNAL_SERVER_ERROR, UNPROCESSABLE_ENTITY
}
