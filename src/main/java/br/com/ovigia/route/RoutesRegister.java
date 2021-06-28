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
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static org.springframework.web.reactive.function.server.ServerResponse.unprocessableEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.BodyBuilder;

import br.com.ovigia.businessrule.Response;
import reactor.core.publisher.Mono;

public class RoutesRegister {
	private static List<RouterFunction<ServerResponse>> routerFunctions = new ArrayList<>();
	private static final RoutesRegister register;
	static {
		register = new RoutesRegister();
	}

	private RoutesRegister() {
	}

	public static RoutesRegister getInstance() {
		return register;
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

	public void registry(Router router) {
		router.getRoutes().forEach(route -> registry(route));

	}

	public <T, V> void registry(Route<T, V> route) {

		var verboHTTP = definirVerboHTTP(route.getTipoRequest(), route.getUrl());

		var routerFunction = route(verboHTTP, request -> {
			if (route.isBodyEnviado() && !route.hasPathVariable()) {
				return fromBody(request, route.getRequestClazz())
						.flatMap(entidade -> toResponse(route.getRule().apply(entidade)));
			} else if (route.isBodyEnviado() && route.hasPathVariable()) {
				return fromBody(request, route.getRequestClazz()).flatMap(entidade -> {
					extractFromPath(request, route.getExtractFromPath(), entidade);
					return toResponse(route.getRule().apply(entidade));

				});
			} else if (!route.isBodyEnviado() && route.hasPathVariable()) {
				T entidade = route.newInstanceRequest();
				extractFromPath(request, route.getExtractFromPath(), entidade);
				return toResponse(route.getRule().apply(entidade));
			}

			return null;
		});
		routerFunctions.add(routerFunction);
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

	private static <T> Mono<T> fromBody(ServerRequest request, Class<T> bodyClazz) {
		return request.bodyToMono(bodyClazz);
	}

	private static <T> T extractFromPath(ServerRequest request, BiFunction<Map<String, String>, T, T> fillFromPAth,
			T entity) {
		if (fillFromPAth == null) {
			return entity;
		}
		var pathVariables = request.pathVariables();
		return fillFromPAth.apply(pathVariables, entity);
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
			} else if (response.isUnauthorized()) {
				bodyBuilder = status(HttpStatus.UNAUTHORIZED);
			}

			return bodyBuilder.bodyValue(response);
		});

	}

}
