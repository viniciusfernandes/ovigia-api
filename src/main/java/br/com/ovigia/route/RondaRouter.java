package br.com.ovigia.route;

import br.com.ovigia.businessrule.exception.DataMalFormatadaException;
import br.com.ovigia.businessrule.ronda.criar.CriarRondaRequest;
import br.com.ovigia.businessrule.ronda.criar.CriarRondaResponse;
import br.com.ovigia.businessrule.ronda.criar.CriarRondaRule;
import br.com.ovigia.businessrule.ronda.obter.ObterRondaRule;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.repository.RondaRepository;

public class RondaRouter extends Router {

	public RondaRouter(RondaRepository repository) {
		var criarRondaRoute = Route.<CriarRondaRequest, CriarRondaResponse>post();
		criarRondaRoute.url("/ovigia/vigias/{idVigia}/rondas").contemBody().requestClass(CriarRondaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).rule(new CriarRondaRule(repository));

		var obterRondaRoute = Route.<IdRonda, Ronda>get();
		obterRondaRoute.url("ovigia/vigias/{idVigia}/rondas/{data}").requestClass(IdRonda.class)
				.extractFromPath((mapa, request) -> {
					try {
						request.data = DataUtil.parseToDataRota(mapa.get("data"));
						request.idVigia = mapa.get("idVigia");
					} catch (DataMalFormatadaException e) {
						e.printStackTrace();
					}
					return request;
				}).rule(new ObterRondaRule(repository));

		addRoute(criarRondaRoute);
		addRoute(obterRondaRoute);
	}

}