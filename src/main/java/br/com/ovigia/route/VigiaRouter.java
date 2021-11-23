package br.com.ovigia.route;

import br.com.ovigia.businessrule.vigia.atualizar.AtualizarVigiaClienteRule;
import br.com.ovigia.businessrule.vigia.atualizar.AtualizarVigiaLocalizacaoRequest;
import br.com.ovigia.businessrule.vigia.atualizar.AtualizarVigiaLocalizacaoRule;
import br.com.ovigia.businessrule.vigia.criar.CriarVigiaRule;
import br.com.ovigia.businessrule.vigia.obter.ObterVigiaRequest;
import br.com.ovigia.businessrule.vigia.obter.ObterVigiaRule;
import br.com.ovigia.businessrule.vigia.obter.ObterVigiasProximosRequest;
import br.com.ovigia.businessrule.vigia.obter.ObterVigiasProximosResponse;
import br.com.ovigia.businessrule.vigia.obter.ObterVigiasProximosRule;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.calculadora.CalculadoraRonda;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.VigiaRepository;

public class VigiaRouter extends Router {
	private final CalculadoraRonda calculadora = CalculadoraRonda.calculadoraEsferica();

	public VigiaRouter(VigiaRepository vigiaRepository, ClienteRepository clienteRepository) {
		var criarCliente = Route.<Vigia, Void>post().path("/ovigia/vigias").contemBody().requestClass(Vigia.class)
				.rule(new CriarVigiaRule(vigiaRepository));

		var obterVigia = Route.<ObterVigiaRequest, Vigia>get().path("/ovigia/vigias/{idVigia}")
				.requestClass(ObterVigiaRequest.class).extractFromPath((mapa, request) -> {
					request.email = mapa.get("idVigia");
					return request;
				}).rule(new ObterVigiaRule(vigiaRepository));

		var obterVigiasProximos = Route.<ObterVigiasProximosRequest, ObterVigiasProximosResponse>get()
				.path("/ovigia/vigias/localizacoes/proximos").requestClass(ObterVigiasProximosRequest.class)
				.extractFromParameters((mapa, request) -> {
					request.latitude = Double.parseDouble(mapa.get("latitude").get(0));
					request.longitude = Double.parseDouble(mapa.get("longitude").get(0));
					return request;
				}).rule(new ObterVigiasProximosRule(vigiaRepository, calculadora));

		var atualizarVigiaCliente = Route.<Cliente, Void>patch().path("/ovigia/vigias/{idVigia}/clientes").contemBody()
				.requestClass(Cliente.class).extractFromPath((mapa, request) -> {
					request.email = mapa.get("idVigia");
					return request;
				}).rule(new AtualizarVigiaClienteRule(vigiaRepository, clienteRepository));

		var atualizarVigiaLoc = Route.<AtualizarVigiaLocalizacaoRequest, Void>patch()
				.path("ovigia/vigias/{idVigia}/localizacao").contemBody()
				.requestClass(AtualizarVigiaLocalizacaoRequest.class).extractFromPath((mapa, request) -> {
					request.email = mapa.get("idVigia");
					return request;
				}).rule(new AtualizarVigiaLocalizacaoRule(vigiaRepository));

		addRoute(criarCliente);
		addRoute(obterVigia);
		addRoute(atualizarVigiaCliente);
		addRoute(atualizarVigiaLoc);
		addRoute(obterVigiasProximos);
	}

}