package br.com.ovigia.route;

import br.com.ovigia.businessrule.exception.DataMalFormatadaException;
import br.com.ovigia.businessrule.rota.CriarLocalizacaoRule;
import br.com.ovigia.businessrule.rota.CriarRondaRequest;
import br.com.ovigia.businessrule.rota.CriarRondaRule;
import br.com.ovigia.businessrule.rota.ObterRotaRule;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.repository.RondaRepository;

public class RotaRouter extends Router {

	public RotaRouter(RondaRepository repository) {
		var criarLocalizacoesRoute = Route.<Localizacao, Void>post();
		criarLocalizacoesRoute.url("/ovigia/vigias/{idVigia}/localizacoes").contemBody().requestClass(Localizacao.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new CriarLocalizacaoRule(repository));

		var criarRotaRoute = Route.<CriarRondaRequest, Void>post();
		criarRotaRoute.url("ovigia/vigias/{idVigia}/rotas").requestClass(CriarRondaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new CriarRondaRule(repository));

		var obterRotaRoute = Route.<IdRonda, Ronda>get();
		obterRotaRoute.url("ovigia/vigias/{idVigia}/rotas/{data}").requestClass(IdRonda.class)
				.extractFromPath((mapa, request) -> {
					try {
						request.setData(DataUtil.parseToDataRota(mapa.get("data")));
						request.setIdVigia(mapa.get("idVigia"));
					} catch (DataMalFormatadaException e) {
						e.printStackTrace();
					}
					return request;
				}).rule(new ObterRotaRule(repository));

		add(criarLocalizacoesRoute);
		add(criarRotaRoute);
		add(obterRotaRoute);
	}

}