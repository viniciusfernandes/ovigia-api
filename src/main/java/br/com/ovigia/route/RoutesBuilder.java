package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import java.util.function.Function;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.ovigia.service.Response;
import reactor.core.publisher.Mono;

public abstract class RoutesBuilder {

	public abstract RouterFunction<ServerResponse> build();

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

	<T, V> Mono<ServerResponse> handleRequest(ServerRequest serverRequest, Function<ServerRequest, T> mapToModel,
			Function<T, Mono<Response<V>>> regraNegocio) {
		var model = mapToModel.apply(serverRequest);
		var response = regraNegocio.apply(model);
		return toBody(response);
	}

}
