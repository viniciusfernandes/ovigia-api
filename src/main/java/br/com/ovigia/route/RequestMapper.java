package br.com.ovigia.route;

import org.springframework.web.reactive.function.server.ServerRequest;

import reactor.core.publisher.Mono;

public interface RequestMapper<V> {
	Mono<V> map(ServerRequest request);
}
