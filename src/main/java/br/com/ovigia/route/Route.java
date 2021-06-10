package br.com.ovigia.route;

import java.util.Map;
import java.util.function.BiFunction;

import br.com.ovigia.businessrule.BusinessRule;

public class Route<T, V> {
	private String url;
	private TipoRequest tipoRequest;
	private Class<T> requestClazz;
	private boolean bodyEnviado;
	private BusinessRule<T, V> rule;
	private BiFunction<Map<String, String>, T, T> extractFromPath;

	private Route(TipoRequest tipoRequest) {
		this.tipoRequest = tipoRequest;
	}

	public T newInstanceRequest() {
		try {
			return requestClazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Route<T, V> url(String url) {
		this.url = url;
		return this;
	}

	public Route<T, V> requestClass(Class<T> requestClazz) {
		this.requestClazz = requestClazz;
		return this;
	}

	public Route<T, V> contemBody() {
		bodyEnviado = true;
		return this;
	}

	public Route<T, V> rule(BusinessRule<T, V> rule) {
		this.rule = rule;
		return this;
	}

	public Route<T, V> extractFromPath(BiFunction<Map<String, String>, T, T> extractFromPath) {
		this.extractFromPath = extractFromPath;
		return this;
	}

	public static <T, V> Route<T, V> post() {
		return new Route<>(TipoRequest.POST);
	}

	public static <T, V> Route<T, V> get() {
		return new Route<>(TipoRequest.GET);
	}

	public static <T, V> Route<T, V> put() {
		return new Route<>(TipoRequest.PUT);
	}

	public static <T, V> Route<T, V> patch() {
		return new Route<>(TipoRequest.PATCH);
	}

	public static <T, V> Route<T, V> delete() {
		return new Route<>(TipoRequest.DELETE);
	}

	public String getUrl() {
		return url;
	}

	public TipoRequest getTipoRequest() {
		return tipoRequest;
	}

	public Class<T> getRequestClazz() {
		return requestClazz;
	}

	public boolean isBodyEnviado() {
		return bodyEnviado;
	}

	public BusinessRule<T, V> getRule() {
		return rule;
	}

	public BiFunction<Map<String, String>, T, T> getExtractFromPath() {
		return extractFromPath;
	}

	public boolean hasPathVariable() {
		return extractFromPath != null;
	}

	public String toString() {
		return tipoRequest + ":" + url;
	}
}

enum TipoRequest {
	POST, PATCH, GET, PUT, DELETE
}