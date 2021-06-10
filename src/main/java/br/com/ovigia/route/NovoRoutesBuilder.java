package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.unprocessableEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;

import br.com.ovigia.businessrule.Response;
import reactor.core.publisher.Mono;

public abstract class NovoRoutesBuilder {
	private List<RouterFunction<ServerResponse>> routerFunctions = new ArrayList<>();

	private <T, V> void buildRoute(RequestHandler<T, V> handler) {
		RequestPredicate verboHTTP = definirVerboHTTP(handler.tipoRequest, handler.url);

		var route = route(verboHTTP, request -> {
			return fromBody(request, handler.bodyClazz).flatMap(entidade -> {
				entidade = fromPath(request, handler.fromPath, entidade);
				return toResponse(handler.rule.apply(entidade));

			});

		});
		routerFunctions.add(route);
	}

	private RequestPredicate definirVerboHTTP(TipoRequest tipoRequest, String url) {
		RequestPredicate verboHTTP = null;
		if (tipoRequest == TipoRequest.POST) {
			verboHTTP = POST(url);
		} else if (tipoRequest == TipoRequest.GET) {
			verboHTTP = GET(url);
		} else if (tipoRequest == TipoRequest.DELETE) {
			verboHTTP = DELETE(url);
		} else if (tipoRequest == TipoRequest.PATCH) {
			verboHTTP = PATCH(url);
		} else if (tipoRequest == TipoRequest.PUT) {
			verboHTTP = PUT(url);
		}
		return verboHTTP;
	}

	private <T> Mono<T> fromBody(ServerRequest request, Class<T> bodyClazz) {
		return request.bodyToMono(bodyClazz);
	}

	private <T> T fromPath(ServerRequest request, BiFunction<Map<String, String>, T, T> fillFromPAth, T entity) {
		if (fillFromPAth == null) {
			return entity;
		}
		var pathVariables = request.pathVariables();
		return fillFromPAth.apply(pathVariables, entity);
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

	private <V> Mono<ServerResponse> toResponse(Mono<Response<V>> mResponse) {
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

}
