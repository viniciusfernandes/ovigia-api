package br.com.ovigia.route;

import br.com.ovigia.businessrule.exception.DataMalFormatadaException;
import br.com.ovigia.businessrule.ronda.CriarLocalizacaoRule;
import br.com.ovigia.businessrule.ronda.CriarRondaRequest;
import br.com.ovigia.businessrule.ronda.CriarRondaRule;
import br.com.ovigia.businessrule.ronda.ObterRondaRule;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.repository.RondaRepository;

public class RondaRouter extends Router {

	public RondaRouter(RondaRepository repository) {
		var criarLocalizacoesRoute = Route.<Localizacao, Void>post();
		criarLocalizacoesRoute.url("/ovigia/vigias/{idVigia}/localizacoes").contemBody().requestClass(Localizacao.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new CriarLocalizacaoRule(repository));

		var criarRondaRoute = Route.<CriarRondaRequest, Void>post();
		criarRondaRoute.url("ovigia/vigias/{idVigia}/rondas").requestClass(CriarRondaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new CriarRondaRule(repository));

		var obterRondaRoute = Route.<IdRonda, Ronda>get();
		obterRondaRoute.url("ovigia/vigias/{idVigia}/rondas/{data}").requestClass(IdRonda.class)
				.extractFromPath((mapa, request) -> {
					try {
						request.setData(DataUtil.parseToDataRota(mapa.get("data")));
						request.setIdVigia(mapa.get("idVigia"));
					} catch (DataMalFormatadaException e) {
						e.printStackTrace();
					}
					return request;
				}).rule(new ObterRondaRule(repository));

		add(criarLocalizacoesRoute);
		add(criarRondaRoute);
		add(obterRondaRoute);
	}

}