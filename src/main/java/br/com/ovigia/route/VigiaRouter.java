package br.com.ovigia.route;

import br.com.ovigia.businessrule.vigia.AtualizarVigiaClienteRule;
import br.com.ovigia.businessrule.vigia.AtualizarVigiaLocalizacaoRequest;
import br.com.ovigia.businessrule.vigia.AtualizarVigiaLocalizacaoRule;
import br.com.ovigia.businessrule.vigia.CriarVigiaRule;
import br.com.ovigia.businessrule.vigia.ObterVigiaRequest;
import br.com.ovigia.businessrule.vigia.ObterVigiaRule;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.VigiaRepository;

public class VigiaRouter extends Router {

	public VigiaRouter(VigiaRepository vigiaRepository, ClienteRepository clienteRepository) {
		var criarClienteRoute = Route.<Vigia, String>post();
		criarClienteRoute.url("/ovigia/vigias").contemBody().requestClass(Vigia.class)
				.rule(new CriarVigiaRule(vigiaRepository));

		var obterVigiaRoute = Route.<ObterVigiaRequest, Vigia>get();
		obterVigiaRoute.url("/ovigia/vigias/{idVigia}").requestClass(ObterVigiaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new ObterVigiaRule(vigiaRepository));

		var atualizarVigiaClienteRoute = Route.<Cliente, Void>patch();
		atualizarVigiaClienteRoute.url("/ovigia/vigias/{idVigia}/clientes").contemBody().requestClass(Cliente.class)
				.extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new AtualizarVigiaClienteRule(vigiaRepository, clienteRepository));

		var atualizarVigiaLocRoute = Route.<AtualizarVigiaLocalizacaoRequest, Void>patch();
		atualizarVigiaLocRoute.url("ovigia/vigias/{idVigia}/localizacao").contemBody()
				.requestClass(AtualizarVigiaLocalizacaoRequest.class).extractFromPath((mapa, request) -> {
					request.setIdVigia(mapa.get("idVigia"));
					return request;
				}).rule(new AtualizarVigiaLocalizacaoRule(vigiaRepository));

		add(criarClienteRoute);
		add(obterVigiaRoute);
		add(atualizarVigiaClienteRoute);
		add(atualizarVigiaLocRoute);
	}

}