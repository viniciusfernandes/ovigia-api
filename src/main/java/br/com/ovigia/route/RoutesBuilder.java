package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Vigia;
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

			if (response.isOk()) {
				return ok().syncBody(response.getValue());
			} else if (response.isNoResult()) {
				return noContent().build();
			} else if (response.isBadRequest()) {
				return badRequest().syncBody(response.getMensagens());
			}

			return status(HttpStatus.INTERNAL_SERVER_ERROR).syncBody(response.getMensagens());
		});
	}

	<T, V> Mono<Response<V>> handleRequest(ServerRequest serverRequest, Class<T> clazz, BusinessRule<T, V> rule) {
		return serverRequest.bodyToMono(clazz).flatMap(model -> rule.apply(model));
	}

}
