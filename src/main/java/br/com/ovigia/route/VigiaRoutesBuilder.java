package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import br.com.ovigia.businessrule.CriarVigiaRule;
import br.com.ovigia.businessrule.ObterVigiaRule;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.VigiaRepository;

public class VigiaRoutesBuilder extends RoutesBuilder {

	public VigiaRoutesBuilder(VigiaRepository vigiaRepository) {
		add(route(POST("/ovigia/vigias"), req -> {
			return toBody(handleRequest(req, Vigia.class, new CriarVigiaRule(null)));
		}));

		add(route(GET("/ovigia/vigias/{idVigia}"), req -> {
			return toBody(handleRequest(req.pathVariable("idVigia"), new ObterVigiaRule(vigiaRepository)));

		}));

	}

}