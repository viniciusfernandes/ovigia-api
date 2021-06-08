package br.com.ovigia.businessrule;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Response<V> {
	private final ResponseStatus status;
	private final V value;

	private Response() {
		status = ResponseStatus.NO_RESULT;
		value = null;
	}

	Response(ResponseStatus status, V value) {
		this.status = status;
		this.value = value;
	}

	private Response(V value) {
		this.status = ResponseStatus.OK;
		this.value = value;
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

	public ResponseStatus getStatus() {
		return status;
	}

	public V getValue() {
		return value;
	}

	public <T> Response<T> clone(T t) {
		return new Response<>(status, t);
	}

	public static <T> Response<T> ok(T value) {
		return new Response<>(value);
	}

	public static <T> Response<T> nonResult() {
		return new Response<>();
	}

	public static <T> Response<T> error(T value, String mensagem) {
		return new ResponseMessage<>(ResponseStatus.INTERNAL_SERVER_ERROR, value, mensagem);
	}

	public static <T> Response<T> unprocessable(T value, String mensagem) {
		return new ResponseMessage<>(ResponseStatus.UNPROCESSABLE_ENTITY, value, mensagem);
	}

	private static class ResponseMessage<V> extends Response<V> {
		private String[] mensagens;

		private ResponseMessage(ResponseStatus status, V value, String... mensagens) {
			super(status, value);
			this.mensagens = mensagens;
		}

	}

}

enum ResponseStatus {
	OK, NO_RESULT, BAD_REQUEST, INTERNAL_SERVER_ERROR, UNPROCESSABLE_ENTITY
}
