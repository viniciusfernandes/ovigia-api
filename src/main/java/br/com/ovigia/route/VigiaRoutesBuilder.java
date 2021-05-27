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

import br.com.ovigia.service.Response;
import br.com.ovigia.service.VigiaService;
import reactor.core.publisher.Mono;

public class VigiaRoutesBuilder {
	private VigiaService vigiaService;

	public VigiaRoutesBuilder(VigiaService vigiaService) {
		this.vigiaService = vigiaService;
	}

	public RouterFunction<ServerResponse> build() {
		return route(GET("/ovigia/vigias"), req -> toBody(vigiaService.obter()))
				.and(route(GET("/ovigia/vigias/{idVigia}"), req -> {
					Integer id = Integer.parseInt(req.pathVariable("idVigia"));

					return toBody(vigiaService.obterById(id));
				}));
	}

	private Mono<ServerResponse> toBody(Mono<Response> mResponse) {
		return mResponse.flatMap(response -> {

			if (response.isOk()) {
				return ok().bodyValue(response.getValue());
			} else if (response.isNoResult()) {
				return noContent().build();
			} else if (response.isBadRequest()) {
				return badRequest().bodyValue(response.getMensagens());
			}

			return status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(response.getMensagens());
		});

	}
}
