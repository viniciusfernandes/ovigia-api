package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.chamado.aceitar.AceitarChamadoRequest;
import br.com.ovigia.businessrule.chamado.aceitar.AceitarChamadoRule;
import br.com.ovigia.businessrule.chamado.cancelar.CancelarChamadoRequest;
import br.com.ovigia.businessrule.chamado.cancelar.CancelarChamadoRule;
import br.com.ovigia.businessrule.chamado.criar.CriarChamadoRequest;
import br.com.ovigia.businessrule.chamado.criar.CriarChamadoRule;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadoAtivoClienteRequest;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadoAtivoClienteResponse;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadoAtivoClienteRule;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosAtivosRequest;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosAtivosVigiaRule;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosVigiaResponse;
import br.com.ovigia.businessrule.common.info.IdInfo;
import br.com.ovigia.model.repository.ChamadoRepository;

public class ChamadoRouter extends Router {

	public ChamadoRouter(ChamadoRepository chamadoRepository) {

		var criarChamado = Route.<CriarChamadoRequest, IdInfo>post().path("/ovigia/clientes/{idCliente}/chamados")
				.contemBody().extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).requestClass(CriarChamadoRequest.class).rule(new CriarChamadoRule(chamadoRepository));

		var obterChamadosAtivosVigia = Route.<ObterChamadosAtivosRequest, List<ObterChamadosVigiaResponse>>get()
				.path("/ovigia/vigias/{idVigia}/chamados-ativos").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(ObterChamadosAtivosRequest.class)
				.rule(new ObterChamadosAtivosVigiaRule(chamadoRepository));

		var obterChamadoAtivoCliente = Route.<ObterChamadoAtivoClienteRequest, ObterChamadoAtivoClienteResponse>get()
				.path("/ovigia/clientes/{idCliente}/chamados/ativos").extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).requestClass(ObterChamadoAtivoClienteRequest.class)
				.rule(new ObterChamadoAtivoClienteRule(chamadoRepository));

		var aceitarChamado = Route.<AceitarChamadoRequest, Void>put().path("/ovigia/vigias/{idChamado}/chamados/aceite")
				.extractFromPath((mapa, request) -> {
					request.idChamado = mapa.get("idChamado");
					return request;
				}).requestClass(AceitarChamadoRequest.class).rule(new AceitarChamadoRule(chamadoRepository));

		var cancelarChamado = Route.<CancelarChamadoRequest, Void>put()
				.path("/ovigia/clientes/{idChamado}/chamados/cancelamento").extractFromPath((mapa, request) -> {
					request.idChamado = mapa.get("idChamado");
					return request;
				}).requestClass(CancelarChamadoRequest.class).rule(new CancelarChamadoRule(chamadoRepository));

		addRoute(criarChamado);
		addRoute(obterChamadosAtivosVigia);
		addRoute(obterChamadoAtivoCliente);
		addRoute(aceitarChamado);
		addRoute(cancelarChamado);

	}

}
