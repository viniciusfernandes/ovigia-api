package br.com.ovigia.route;

import br.com.ovigia.businessrule.vigia.atualizar.AtualizarVigiaClienteRule;
import br.com.ovigia.businessrule.vigia.atualizar.AtualizarVigiaLocalizacaoRequest;
import br.com.ovigia.businessrule.vigia.atualizar.AtualizarVigiaLocalizacaoRule;
import br.com.ovigia.businessrule.vigia.criar.CriarVigiaRule;
import br.com.ovigia.businessrule.vigia.obter.ObterVigiaRequest;
import br.com.ovigia.businessrule.vigia.obter.ObterVigiaRule;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.VigiaRepository;

public class VigiaRouter extends Router {

	public VigiaRouter(VigiaRepository vigiaRepository, ClienteRepository clienteRepository) {
		var criarCliente = Route.<Vigia, Void>post();
		criarCliente.url("/ovigia/vigias").contemBody().requestClass(Vigia.class)
				.rule(new CriarVigiaRule(vigiaRepository));

		var obterVigia = Route.<ObterVigiaRequest, Vigia>get();
		obterVigia.url("/ovigia/vigias/{idVigia}").requestClass(ObterVigiaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.email = mapa.get("idVigia");
					return request;
				}).rule(new ObterVigiaRule(vigiaRepository));

		var obterVigiaLocalizacaoRoute = Route.<ObterVigiaRequest, Vigia>get();
		obterVigia.url("/ovigia/vigias/localizacao").requestClass(ObterVigiaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.email = mapa.get("idVigia");
					return request;
				}).rule(new ObterVigiaRule(vigiaRepository));

		
		var atualizarVigiaCliente = Route.<Cliente, Void>patch();
		atualizarVigiaCliente.url("/ovigia/vigias/{idVigia}/clientes").contemBody().requestClass(Cliente.class)
				.extractFromPath((mapa, request) -> {
					request.email = mapa.get("idVigia");
					return request;
				}).rule(new AtualizarVigiaClienteRule(vigiaRepository, clienteRepository));

		var atualizarVigiaLoc = Route.<AtualizarVigiaLocalizacaoRequest, Void>patch();
		atualizarVigiaLoc.url("ovigia/vigias/{idVigia}/localizacao").contemBody()
				.requestClass(AtualizarVigiaLocalizacaoRequest.class).extractFromPath((mapa, request) -> {
					request.email = mapa.get("idVigia");
					return request;
				}).rule(new AtualizarVigiaLocalizacaoRule(vigiaRepository));

		addRoute(criarCliente);
		addRoute(obterVigia);
		addRoute(atualizarVigiaCliente);
		addRoute(atualizarVigiaLoc);
	//	addRoute(obterVigiaLocalizacaoRoute );
	}

}