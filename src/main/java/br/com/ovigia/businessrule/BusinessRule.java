package br.com.ovigia.businessrule;

import reactor.core.publisher.Mono;

public interface BusinessRule<T, V> {

	Mono<Response<V>> apply(T t);

}
