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

public class RoutesBuilder {
	private static List<RouterFunction<ServerResponse>> routerFunctions = new ArrayList<>();
	private static final RoutesBuilder register;
	static {
		register = new RoutesBuilder();
	}

	private RoutesBuilder() {
	}

	public static RoutesBuilder getInstance() {
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

	public void addRouter(Router router) {
		router.getRoutes().forEach(route -> addRoute(route));

	}

	public <T, V> void addRoute(Route<T, V> route) {
		var verboHTTP = definirVerboHTTP(route.getTipoRequest(), route.getUrl());
		var routerFunction = route(verboHTTP, request -> {
			Mono<T> mono = null;
			if (route.hasBody()) {
				mono = fromBody(request, route.getRequestClazz());
			} else {
				mono = Mono.just(route.newInstanceRequest());
			}

			if (route.hasPathVariable()) {
				mono = mono
						.map(businessRequest -> extractFromPath(request, route.getExtractFromPath(), businessRequest));
			}

			if (route.hasParameters()) {
				mono = mono.map(businessRequest -> extractFromParamaters(request, route.getExtractFromParameters(),
						businessRequest));
			}

			return mono.flatMap(businessRequest -> route.getRule().apply(businessRequest))
					.flatMap(this::handleResponse);

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

	private static <T> T extractFromParamaters(ServerRequest request,
			BiFunction<Map<String, String>, T, T> fillFromParameters, T entity) {
		if (fillFromParameters == null) {
			return entity;
		}
		var pathVariables = request.pathVariables();
		return fillFromParameters.apply(pathVariables, entity);
	}

	private <V> Mono<ServerResponse> handleResponse(Response<V> response) {
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
	}

	public void printRoutes() {
		routerFunctions.forEach(r -> System.out.println(r));
	}
}
