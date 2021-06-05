package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.ovigia.businessrule.CriarLocalizacaoRule;
import br.com.ovigia.businessrule.CriarRotaRule;
import br.com.ovigia.businessrule.ObterRotaRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.IdRota;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.RotaRepository;
import reactor.core.publisher.Mono;

public class RotaRoutesBuilder extends RoutesBuilder {
	private final DateFormat format = new SimpleDateFormat("dd-MM-yyyy");

	public RotaRoutesBuilder(RotaRepository repository) {
		add(route(POST("/ovigia/vigias/{idVigia}/localizacoes"), req -> {
			return toBody(handleRequest(req, Localizacao.class, new CriarLocalizacaoRule(repository)));
		}));

		add(route(POST("/ovigia/vigias/{idVigia}/rotas"), req -> {
			return toBody(handleRequest(req.pathVariable("idVigia"), new CriarRotaRule(repository)));
		}));

		add(route(GET("/ovigia/vigias/{idVigia}/rotas/{data}"), req -> {
			var idVigia = req.pathVariable("idVigia");
			Date data;
			try {
				data = format.parse(req.pathVariable("data"));
			} catch (ParseException e) {
				var responseError = Response.error(req.pathVariable("data"),
						"A data de pesquisa nao esta formatada corretamente");
				return toBody(Mono.just(responseError));
			}
			var idRota = new IdRota(idVigia, data);
			return toBody(handleRequest(idRota, new ObterRotaRule(repository)));

		}));

	}

}