package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.ovigia.service.ClienteService;
import br.com.ovigia.service.Response;
import reactor.core.publisher.Mono;

public class ClienteRoutesBuilder {
	private ClienteService service;

	public ClienteRoutesBuilder(ClienteService service) {
		this.service = service;
	}

	public RouterFunction<ServerResponse> build() {
		return route(GET("/ovigia/clientes"), req -> toBody(Mono.just(new Response())));
	}

	private Mono<ServerResponse> toBody(Mono<Response> mResponse) {
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
}
