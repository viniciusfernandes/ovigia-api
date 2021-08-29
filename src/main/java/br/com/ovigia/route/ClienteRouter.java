package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.cliente.atualizar.AtualizarClienteLocalizacaoRequest;
import br.com.ovigia.businessrule.cliente.atualizar.AtualizarClienteLocalizacaoRule;
import br.com.ovigia.businessrule.cliente.calcular.CalcularFrequenciaRondaRequest;
import br.com.ovigia.businessrule.cliente.calcular.CalcularFrequenciaRondaResponse;
import br.com.ovigia.businessrule.cliente.calcular.CalcularFrequenciaRondasRule;
import br.com.ovigia.businessrule.cliente.criar.CriarClienteRule;
import br.com.ovigia.businessrule.exception.DataMalFormatadaException;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.RondaRepository;

public class ClienteRouter extends Router {

	public ClienteRouter(ClienteRepository clienteRepository, RondaRepository rotaRepository) {

		var criarClienteRoute = Route.<Cliente, Void>post();
		criarClienteRoute.url("/ovigia/clientes").contemBody().requestClass(Cliente.class)
				.rule(new CriarClienteRule(clienteRepository));

		var calcularFreqRoute = Route.<CalcularFrequenciaRondaRequest, List<CalcularFrequenciaRondaResponse>>get();
		calcularFreqRoute.url("ovigia/clientes/{idCliente}/frequencia-rondas/{data}")
				.requestClass(CalcularFrequenciaRondaRequest.class)
				.rule(new CalcularFrequenciaRondasRule(clienteRepository, rotaRepository))
				.extractFromPath((mapa, request) -> {
					try {
						request.setDataRonda(DataUtil.parseToDataRota(mapa.get("data")));
						request.setIdCliente(mapa.get("idCliente"));
					} catch (DataMalFormatadaException e) {
						throw new IllegalArgumentException(e);
					}
					return request;
				});

		var alterarLocalizacaoRoute = Route.<AtualizarClienteLocalizacaoRequest, Void>patch();
		alterarLocalizacaoRoute.url("ovigia/clientes/{idCliente}/localizacao").contemBody()
				.requestClass(AtualizarClienteLocalizacaoRequest.class).extractFromPath((mapa, request) -> {
					request.idCliente = mapa.get("idCliente");
					return request;
				}).rule(new AtualizarClienteLocalizacaoRule(clienteRepository));

		addRoute(criarClienteRoute);
		addRoute(calcularFreqRoute);
		addRoute(alterarLocalizacaoRoute);

	}

}
