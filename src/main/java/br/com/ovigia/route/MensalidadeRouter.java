package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.mensalidade.ObterMensalidadesVencidasRequest;
import br.com.ovigia.businessrule.mensalidade.ObterMensalidadesVencidasResponse;
import br.com.ovigia.businessrule.mensalidade.ObterMensalidadesVencidasRule;
import br.com.ovigia.model.repository.MensalidadeRepository;

public class MensalidadeRouter extends Router {

	public MensalidadeRouter(MensalidadeRepository mensalidadeRepository) {

		var obterMensalidadesVencidas = Route
				.<ObterMensalidadesVencidasRequest, List<ObterMensalidadesVencidasResponse>>get()
				.path("/ovigia/vigias/{idVigia}/mensalidades-vencidas").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(ObterMensalidadesVencidasRequest.class)
				.rule(new ObterMensalidadesVencidasRule(mensalidadeRepository));

		addRoute(obterMensalidadesVencidas);

	}

}