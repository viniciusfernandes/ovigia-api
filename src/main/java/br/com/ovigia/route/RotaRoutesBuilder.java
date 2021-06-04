package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import br.com.ovigia.businessrule.CriarVigiaRule;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.VigiaRepository;

public class RotaRoutesBuilder extends RoutesBuilder {

	public RotaRoutesBuilder(VigiaRepository vigiaRepository) {
		add(route(POST("/ovigia/vigias/{idVigia}/rota"), req -> {
			return toBody(handleRequest(req, Vigia.class, new CriarVigiaRule(vigiaRepository)));
		}));

		 
	}

}