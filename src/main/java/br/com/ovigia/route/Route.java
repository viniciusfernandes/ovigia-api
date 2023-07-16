package br.com.ovigia.route;

import br.com.ovigia.businessrule.BusinessRule;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class Route<T, V> {
	private String path;
	private TipoRequest tipoRequest;
	private Class<T> requestClazz;
	private boolean bodyEnviado;
	private BusinessRule<T, V> rule;
	private BiFunction<Map<String, String>, T, T> extractFromPath;
	private BiFunction<Map<String, List<String>>, T, T> extractFromParameters;

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

	public Route<T, V> path(String path) {
		this.path = path;
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

	public Route<T, V> extractFromParameters(BiFunction<Map<String, List<String>>, T, T> extractFromParameters) {
		this.extractFromParameters = extractFromParameters;
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

	public String getPath() {
		return path;
	}

	public TipoRequest getTipoRequest() {
		return tipoRequest;
	}

	public Class<T> getRequestClazz() {
		return requestClazz;
	}

	public boolean hasBody() {
		return bodyEnviado;
	}

	public BusinessRule<T, V> getRule() {
		return rule;
	}

	public BiFunction<Map<String, String>, T, T> getExtractFromPath() {
		return extractFromPath;
	}

	public BiFunction<Map<String, List<String>>, T, T> getExtractFromParameters() {
		return extractFromParameters;
	}

	public boolean hasPathVariable() {
		return extractFromPath != null;
	}

	public boolean hasParameters() {
		return extractFromParameters != null;
	}

	public String toString() {
		return tipoRequest + ":" + path;
	}
}

enum TipoRequest {
	POST, PATCH, GET, PUT, DELETE
}