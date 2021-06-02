package br.com.ovigia.businessrule;

import java.util.ArrayList;
import java.util.List;

public class Response<V> {
	private final ResponseStatus status;
	private final V value;
	private List<String> mensagens;

	public Response() {
		status = ResponseStatus.OK;
		value = null;
		mensagens = null;
	}

	public Response(ResponseStatus status, V value, List<String> mensagens) {
		this.status = status;
		this.value = value;
		this.mensagens = mensagens;
	}

	public Response(ResponseStatus status, V value) {
		this.status = status;
		this.value = value;
		this.mensagens = null;
	}

	public Response(ResponseStatus status, List<String> mensagens) {
		this.status = status;
		this.value = null;
		this.mensagens = mensagens;
	}

	public Response(V value) {
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

	public boolean isBadRequest() {
		return ResponseStatus.BAD_REQUEST == status;
	}

	public boolean isOk() {
		return ResponseStatus.OK == status;
	}

	public boolean isNoResult() {
		return ResponseStatus.NO_RESULT == status;
	}

	public boolean isServerError() {
		return ResponseStatus.SERVER_ERROR == status;
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
}

enum ResponseStatus {
	OK, NO_RESULT, BAD_REQUEST, SERVER_ERROR
}
