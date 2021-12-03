package br.com.ovigia.route;

import br.com.ovigia.businessrule.cliente.atualizar.AtualizarClienteLocalizacaoRequest;
import br.com.ovigia.businessrule.cliente.atualizar.AtualizarClienteLocalizacaoRule;
import br.com.ovigia.businessrule.cliente.criar.CriarClienteRule;
import br.com.ovigia.businessrule.cliente.obter.ObterIdVigiaClienteRequest;
import br.com.ovigia.businessrule.cliente.obter.ObterIdVigiaClienteResponse;
import br.com.ovigia.businessrule.cliente.obter.ObterIdVigiaClienteRule;
import br.com.ovigia.businessrule.frequenciaronda.commom.business.CriarFrequenciaRondasBusiness;
import br.com.ovigia.businessrule.frequenciaronda.obter.ObterFrequenciaRondaRequest;
import br.com.ovigia.businessrule.frequenciaronda.obter.ObterFrequenciaRondaResponse;
import br.com.ovigia.businessrule.frequenciaronda.obter.ObterFrequenciaRondaRule;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.repository.ClienteRepository;

public class ClienteRouter extends Router {

	public ClienteRouter(ClienteRepository clienteRepository,
			CriarFrequenciaRondasBusiness criarFrequenciaRondasBusiness) {

		var criarCliente = Route.<Cliente, Void>post().path("/ovigia/clientes").contemBody().requestClass(Cliente.class)
				.rule(new CriarClienteRule(clienteRepository));

		var alterarLocalizacao = Route.<AtualizarClienteLocalizacaoRequest, Void>patch()
				.path("ovigia/clientes/{idCliente}/localizacao").contemBody()
				.requestClass(AtualizarClienteLocalizacaoRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new AtualizarClienteLocalizacaoRule(clienteRepository));

		var obterFrequenciaRonda = Route.<ObterFrequenciaRondaRequest, ObterFrequenciaRondaResponse>get()
				.path("/ovigia/clientes/{idCliente}/frequencia-ronda").requestClass(ObterFrequenciaRondaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new ObterFrequenciaRondaRule(clienteRepository, criarFrequenciaRondasBusiness));

		var obterIdVigia = Route.<ObterIdVigiaClienteRequest, ObterIdVigiaClienteResponse>get()
				.path("/ovigia/clientes/{idCliente}/id-vigia").requestClass(ObterIdVigiaClienteRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new ObterIdVigiaClienteRule(clienteRepository));

		addRoute(criarCliente);
		addRoute(alterarLocalizacao);
		addRoute(obterFrequenciaRonda);
		addRoute(obterIdVigia);
	}

}
