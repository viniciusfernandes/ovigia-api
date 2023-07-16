package br.com.ovigia.route;

import br.com.ovigia.businessrule.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerResponse.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.*;

public class RoutesBuilder {
	private static List<RouterFunction<ServerResponse>> routerFunctions = new ArrayList<>();
	private static final RoutesBuilder register;
	private final Logger logger = LoggerFactory.getLogger(getClass());
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
		var verboHTTP = definirVerboHTTP(route.getTipoRequest(), route.getPath());
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

	private static <T> T extractFromPath(ServerRequest serverRequest,
			BiFunction<Map<String, String>, T, T> fillFromPath, T businessRequest) {
		if (fillFromPath == null) {
			return businessRequest;
		}
		var pathVariables = serverRequest.pathVariables();
		return fillFromPath.apply(pathVariables, businessRequest);
	}

	private static <T> T extractFromParamaters(ServerRequest request,
			BiFunction<Map<String, List<String>>, T, T> fillFromParameters, T entity) {
		if (fillFromParameters == null) {
			return entity;
		}

		var mapa = request.queryParams();
		return fillFromParameters.apply(mapa, entity);
	}

	private <V> Mono<ServerResponse> handleResponse(Response<V> response) {
		if (response.isNoContent()) {
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
		routerFunctions.forEach(route -> logger.info(String.valueOf(route)));
	}
}
