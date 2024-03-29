package br.com.ovigia.route;

import br.com.ovigia.businessrule.mensalidade.obter.*;
import br.com.ovigia.businessrule.mensalidade.pagar.PagarMensalidadeRequest;
import br.com.ovigia.businessrule.mensalidade.pagar.PagarMensalidadeRule;
import br.com.ovigia.model.repository.FaturamentoRepository;
import br.com.ovigia.model.repository.MensalidadeRepository;

import java.util.List;

public class MensalidadeRouter extends Router {

	public MensalidadeRouter(MensalidadeRepository mensalidadeRepository, FaturamentoRepository faturamentoRepository) {

		var obterMensalidadesVencidas = Route
				.<ObterMensalidadesVencidasRequest, List<ObterMensalidadesVencidasResponse>>get()
				.path("/ovigia/vigias/{idVigia}/mensalidades-vencidas").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(ObterMensalidadesVencidasRequest.class)
				.rule(new ObterMensalidadesVencidasRule(mensalidadeRepository));

		var pagarMensalidade = Route.<PagarMensalidadeRequest, Void>patch()
				.path("/ovigia/mensalidades/{idMensalidade}/pagamento").contemBody()
				.extractFromPath((mapa, request) -> {
					request.idMensalidade = mapa.get("idMensalidade");
					return request;
				}).requestClass(PagarMensalidadeRequest.class)
				.rule(new PagarMensalidadeRule(mensalidadeRepository, faturamentoRepository));

		var obterValorRecebido = Route.<ObterValorRecebidoAtualRequest, ObterValorRecebidoAtualResponse>get()
				.path("/ovigia/vigias/{idVigia}/valor-recebido").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(ObterValorRecebidoAtualRequest.class)
				.rule(new ObterValorRecebidoAtualRule(faturamentoRepository));

		addRoute(obterMensalidadesVencidas);
		addRoute(pagarMensalidade);
		addRoute(obterValorRecebido);
	}

}