package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.chamado.aceitar.AceitarChamadoRequest;
import br.com.ovigia.businessrule.chamado.aceitar.AceitarChamadoRule;
import br.com.ovigia.businessrule.chamado.cancelar.CancelarChamadoRequest;
import br.com.ovigia.businessrule.chamado.cancelar.CancelarChamadoRule;
import br.com.ovigia.businessrule.chamado.criar.CriarChamadoRequest;
import br.com.ovigia.businessrule.chamado.criar.CriarChamadoRule;
import br.com.ovigia.businessrule.chamado.encerrar.EncerrarChamadoRequest;
import br.com.ovigia.businessrule.chamado.encerrar.EncerrarChamadoRule;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadoAtivoClienteRequest;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadoAtivoClienteResponse;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadoAtivoClienteRule;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosAbertosRequest;
import br.com.ovigia.businessrule.chamado.obter.ObterChamadosAbertosVigiaRule;
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

		var obterChamadosAbertosVigia = Route.<ObterChamadosAbertosRequest, List<ObterChamadosVigiaResponse>>get()
				.path("/ovigia/vigias/{idVigia}/chamados-abertos").extractFromPath((mapa, request) -> {
					request.idVigia = mapa.get("idVigia");
					return request;
				}).requestClass(
						ObterChamadosAbertosRequest.class)
				.rule(new ObterChamadosAbertosVigiaRule(chamadoRepository));

		var obterChamadoAtivoCliente = Route.<ObterChamadoAtivoClienteRequest, ObterChamadoAtivoClienteResponse>get()
				.path("/ovigia/clientes/{idCliente}/chamados/ativos").extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).requestClass(ObterChamadoAtivoClienteRequest.class)
				.rule(new ObterChamadoAtivoClienteRule(chamadoRepository));

		var aceitarChamado = Route.<AceitarChamadoRequest, Void>patch()
				.path("/ovigia/vigias/chamados/{idChamado}/aceite").extractFromPath((mapa, request) -> {
					request.idChamado = mapa.get("idChamado");
					return request;
				}).requestClass(AceitarChamadoRequest.class).rule(new AceitarChamadoRule(chamadoRepository));

		var cancelarChamado = Route.<CancelarChamadoRequest, Void>patch()
				.path("/ovigia/clientes/chamados/{idChamado}/cancelamento").extractFromPath((mapa, request) -> {
					request.idChamado = mapa.get("idChamado");
					return request;
				}).requestClass(CancelarChamadoRequest.class).rule(new CancelarChamadoRule(chamadoRepository));

		var encerrarChamado = Route.<EncerrarChamadoRequest, Void>patch()
				.path("/ovigia/clientes/chamados/{idChamado}/encerramento").extractFromPath((mapa, request) -> {
					request.idChamado = mapa.get("idChamado");
					return request;
				}).requestClass(EncerrarChamadoRequest.class).rule(new EncerrarChamadoRule(chamadoRepository));

		addRoute(criarChamado);
		addRoute(obterChamadosAbertosVigia);
		addRoute(obterChamadoAtivoCliente);
		addRoute(aceitarChamado);
		addRoute(cancelarChamado);
		addRoute(encerrarChamado);
	}

}
