package br.com.ovigia.route;

import br.com.ovigia.businessrule.Response;
import reactor.core.publisher.Mono;

public interface RequestHandler<T, V> {
	Mono<Response<V>> handle(T t);
}
