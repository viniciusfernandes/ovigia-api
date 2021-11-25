package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.contrato.criar.CriarContratoRequest;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoResponse;
import br.com.ovigia.businessrule.contrato.criar.CriarContratoRule;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoAtivoClienteRequest;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoAtivoClienteResponse;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoAtivoClienteRule;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosRequest;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosResponse;
import br.com.ovigia.businessrule.contrato.obter.ObterContratoVencidosRule;
import br.com.ovigia.businessrule.contrato.remover.RemoverContratoRequest;
import br.com.ovigia.businessrule.contrato.remover.RemoverContratoRule;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import br.com.ovigia.model.repository.VigiaRepository;

public class ContratoRouter extends Router {

	public ContratoRouter(ContratoRepository contratoRepository,
			SolicitacaoVisitaRepository solicitacaoVisitaRepository, VigiaRepository vigiaRepository) {
		var criarContrato = Route.<CriarContratoRequest, CriarContratoResponse>post().path("/ovigia/contratos")
				.contemBody().requestClass(CriarContratoRequest.class)
				.rule(new CriarContratoRule(contratoRepository, solicitacaoVisitaRepository));

		var obterContratosVencidos = Route.<ObterContratoVencidosRequest, List<ObterContratoVencidosResponse>>get()
				.path("/ovigia/vigias/{idVigia}/contratos-vencidos").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(ObterContratoVencidosRequest.class)
				.rule(new ObterContratoVencidosRule(contratoRepository));

		var obterContratoAtivoCliente = Route.<ObterContratoAtivoClienteRequest, ObterContratoAtivoClienteResponse>get()
				.path("/ovigia/clientes/{idCliente}/contrato-ativo").extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).requestClass(ObterContratoAtivoClienteRequest.class)
				.rule(new ObterContratoAtivoClienteRule(contratoRepository, vigiaRepository));

		var removerContrato = Route.<RemoverContratoRequest, Void>delete().path("/ovigia/contratos/{idContrato}")
				.extractFromPath((mapa, request) -> {
					request.idContrato = mapa.get("idContrato");
					return request;
				}).rule(new RemoverContratoRule());

		addRoute(criarContrato);
		addRoute(obterContratosVencidos);
		addRoute(obterContratoAtivoCliente);
		addRoute(removerContrato);
	}

}