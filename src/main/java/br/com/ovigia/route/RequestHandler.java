package br.com.ovigia.route;

import java.util.Map;
import java.util.function.BiFunction;

import br.com.ovigia.businessrule.BusinessRule;

public class RequestHandler<T, V> {
	public String url;
	public TipoRequest tipoRequest;
	public Class<T> bodyClazz;
	public BusinessRule<T, V> rule;
	public BiFunction<Map<String, String>, T, T> fromPath;
}

enum TipoRequest {
	POST, PATCH, GET, PUT, DELETE
}