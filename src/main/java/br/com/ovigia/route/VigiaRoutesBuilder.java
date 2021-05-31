package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.noContent;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import br.com.ovigia.model.Vigia;
import br.com.ovigia.service.Response;
import br.com.ovigia.service.VigiaService;
import reactor.core.publisher.Mono;

public class VigiaRoutesBuilder {
	private VigiaService vigiaService;

	public VigiaRoutesBuilder(VigiaService vigiaService) {
		this.vigiaService = vigiaService;
	}

	public RouterFunction<ServerResponse> build() {
		return route(GET("/ovigia/vigias"), req -> toBody(vigiaService.buscar()))
				.and(route(GET("/ovigia/vigias/{idVigia}"), req -> {
					return toBody(vigiaService.buscarPorId(req.pathVariable("idVigia"))
							.map(resp -> mapToRequest((Vigia) resp.getValue())));
				})).and(route(POST("/ovigia/vigias"), req -> {
					return mapToModel(req).map(vigia -> vigiaService.salvar(vigia)).flatMap(resp -> toBody(resp));
				}));
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

	private Mono<Vigia> mapToModel(ServerRequest serverRequest) {
		return serverRequest.body(BodyExtractors.toMono(VigiaRequest.class)).map(req -> {
			var vigia = new Vigia();
			vigia.setEmail(req.getEmail());
			vigia.setNome(req.getNome());
			vigia.setTelefone(req.getTelefone());
			return vigia;
		});
	}

	private Response mapToRequest(Vigia vigia) {
		var req = new VigiaRequest();
		req.setEmail(vigia.getEmail());
		req.setNome(vigia.getNome());
		req.setTelefone(vigia.getTelefone());
		return new Response(req);
	}
}
