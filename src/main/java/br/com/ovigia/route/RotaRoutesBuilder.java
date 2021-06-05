package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import br.com.ovigia.businessrule.CriarLocalizacaoRule;
import br.com.ovigia.businessrule.CriarRotaRule;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Rota;
import br.com.ovigia.repository.RotaRepository;

public class RotaRoutesBuilder extends RoutesBuilder {

	public RotaRoutesBuilder(RotaRepository repository) {
		add(route(POST("/ovigia/vigias/{idVigia}/localizacoes"), req -> {
			return toBody(handleRequest(req, Localizacao.class, new CriarLocalizacaoRule(repository)));
		}));

		add(route(POST("/ovigia/vigias/{idVigia}/rotas"), req -> {
			return toBody(handleRequest(req.pathVariable("idVigia"), new CriarRotaRule(repository)));
		}));

	}

}