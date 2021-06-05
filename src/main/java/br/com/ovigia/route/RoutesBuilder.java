package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.unprocessableEntity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import reactor.core.publisher.Mono;

public abstract class RoutesBuilder {
	private List<RouterFunction<ServerResponse>> routerFunctions = new ArrayList<>();

	RoutesBuilder add(RouterFunction<ServerResponse> route) {
		routerFunctions.add(route);
		return this;
	}

	public RouterFunction<ServerResponse> build() {
		RouterFunction<ServerResponse> routes = null;
		for (var route : routerFunctions) {
			if (routes == null) {
				routes = route;
				continue;
			}
			routes = routes.and(route);
		}
		return routes;
	}

	<V> Mono<ServerResponse> toBody(Mono<Response<V>> mResponse) {
		return mResponse.flatMap(response -> {

			if (response.isNoResult()) {
				return noContent().build();
			}

			BodyBuilder bodyBuilder = null;
			if (response.isOk()) {
				bodyBuilder = ok();
			} else if (response.isBadRequest()) {
				bodyBuilder = badRequest();
			} else if (response.isUnprocessable()) {
				bodyBuilder = unprocessableEntity();
			}

			return bodyBuilder.bodyValue(response);
		});
	}

	<T, V> Mono<Response<V>> handleRequest(ServerRequest serverRequest, Class<T> requestClazz,
			BusinessRule<T, V> rule) {
		return serverRequest.bodyToMono(requestClazz).flatMap(model -> rule.apply(model));
	}

	<T, V> Mono<Response<V>> handleRequest(T t, BusinessRule<T, V> rule) {
		return rule.apply(t);
	}
}
