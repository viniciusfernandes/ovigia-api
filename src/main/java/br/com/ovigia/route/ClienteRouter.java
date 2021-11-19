package br.com.ovigia.route;

import br.com.ovigia.businessrule.cliente.atualizar.AtualizarClienteLocalizacaoRequest;
import br.com.ovigia.businessrule.cliente.atualizar.AtualizarClienteLocalizacaoRule;
import br.com.ovigia.businessrule.cliente.criar.CriarClienteRule;
import br.com.ovigia.businessrule.cliente.obter.ObterFrequenciaRondaRequest;
import br.com.ovigia.businessrule.cliente.obter.ObterFrequenciaRondaResponse;
import br.com.ovigia.businessrule.cliente.obter.ObterFrequenciaRondaRule;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.RondaRepository;

public class ClienteRouter extends Router {

	public ClienteRouter(ClienteRepository clienteRepository, RondaRepository rotaRepository) {

		var criarCliente = Route.<Cliente, Void>post().url("/ovigia/clientes").contemBody().requestClass(Cliente.class)
				.rule(new CriarClienteRule(clienteRepository));

		var alterarLocalizacao = Route.<AtualizarClienteLocalizacaoRequest, Void>patch()
				.url("ovigia/clientes/{idCliente}/localizacao").contemBody()
				.requestClass(AtualizarClienteLocalizacaoRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new AtualizarClienteLocalizacaoRule(clienteRepository));

		var obterFrequenciaRonda = Route.<ObterFrequenciaRondaRequest, ObterFrequenciaRondaResponse>get()
				.url("/ovigia/clientes/{idCliente}/frequencia-ronda").requestClass(ObterFrequenciaRondaRequest.class)
				.extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new ObterFrequenciaRondaRule(clienteRepository));

		addRoute(criarCliente);
		addRoute(alterarLocalizacao);
		addRoute(obterFrequenciaRonda);
	}

}
