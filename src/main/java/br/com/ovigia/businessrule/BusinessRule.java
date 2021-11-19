package br.com.ovigia.businessrule;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface BusinessRule<T, V> {

	Mono<Response<V>> apply(T request);

}
