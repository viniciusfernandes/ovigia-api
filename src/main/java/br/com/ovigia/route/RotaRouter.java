package br.com.ovigia.route;

import br.com.ovigia.businessrule.exception.DataRotaMalFormatadaException;
import br.com.ovigia.businessrule.rota.CriarLocalizacaoRule;
import br.com.ovigia.businessrule.rota.CriarRotaRequest;
import br.com.ovigia.businessrule.rota.CriarRotaRule;
import br.com.ovigia.businessrule.rota.ObterRotaRule;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdRota;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Rota;
import br.com.ovigia.repository.RotaRepository;

public class RotaRouter extends Router {

	public RotaRouter(RotaRepository repository) {
		var criarLocalizacoesRoute = Route.<Localizacao, Void>post();
		criarLocalizacoesRoute.url("/ovigia/vigias/{idVigia}/localizacoes").contemBody().requestClass(Localizacao.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new CriarLocalizacaoRule(repository));

		var criarRotaRoute = Route.<CriarRotaRequest, Void>post();
		criarRotaRoute.url("ovigia/vigias/{idVigia}/rotas").requestClass(CriarRotaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new CriarRotaRule(repository));

		var obterRotaRoute = Route.<IdRota, Rota>get();
		obterRotaRoute.url("ovigia/vigias/{idVigia}/rotas/{data}").requestClass(IdRota.class)
				.extractFromPath((mapa, request) -> {
					try {
						request.setData(DataUtil.parseToDataRota(mapa.get("data")));
						request.setIdVigia(mapa.get("idVigia"));
					} catch (DataRotaMalFormatadaException e) {
						e.printStackTrace();
					}
					return request;
				}).rule(new ObterRotaRule(repository));

		add(criarLocalizacoesRoute);
		add(criarRotaRoute);
		add(obterRotaRoute);
	}

}