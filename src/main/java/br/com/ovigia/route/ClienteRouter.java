package br.com.ovigia.route;

import java.util.List;

import br.com.ovigia.businessrule.cliente.AtualizarClienteLocalizacaoRequest;
import br.com.ovigia.businessrule.cliente.AtualizarClienteLocalizacaoRule;
import br.com.ovigia.businessrule.cliente.CalcularFrequenciaRondaRequest;
import br.com.ovigia.businessrule.cliente.CalcularFrequenciaRondaResponse;
import br.com.ovigia.businessrule.cliente.CalcularFrequenciaRondasRule;
import br.com.ovigia.businessrule.cliente.CriarClienteRule;
import br.com.ovigia.businessrule.exception.DataRotaMalFormatadaException;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.RotaRepository;

public class ClienteRouter extends Router {

	public ClienteRouter(ClienteRepository clienteRepository, RotaRepository rotaRepository) {

		var criarClienteRoute = Route.<Cliente, String>post();
		criarClienteRoute.url("/ovigia/clientes").contemBody().requestClass(Cliente.class)
				.rule(new CriarClienteRule(clienteRepository));

		var calcularFreqRoute = Route.<CalcularFrequenciaRondaRequest, List<CalcularFrequenciaRondaResponse>>get();
		calcularFreqRoute.url("ovigia/clientes/{idCliente}/frequencia-rondas/{data}")
				.requestClass(CalcularFrequenciaRondaRequest.class)
				.rule(new CalcularFrequenciaRondasRule(clienteRepository, rotaRepository))
				.extractFromPath((mapa, calculo) -> {
					try {
						calculo.setDataRonda(DataUtil.parseToDataRota(mapa.get("data")));
						calculo.setIdCliente(mapa.get("idCliente"));
					} catch (DataRotaMalFormatadaException e) {
						e.printStackTrace();
						String mensagem = String.format("A data enviada nao esta no padrao %s. Tente novamente.",
								DataUtil.dataRotaPadrao);
						// var mResponse = Mono.just(Response.unprocessable(req.pathVariable("data"),
						// mensagem));
						// return toBody(mResponse);
					}
					return calculo;
				});

		var alterarLocalizacaoRoute = Route.<AtualizarClienteLocalizacaoRequest, Void>patch();
		alterarLocalizacaoRoute.url("ovigia/clientes/{idCliente}/localizacao").contemBody()
				.requestClass(AtualizarClienteLocalizacaoRequest.class).extractFromPath((mapa, localizacao) -> {
					localizacao.idCliente = mapa.get("idCliente");
					return localizacao;
				}).rule(new AtualizarClienteLocalizacaoRule(clienteRepository));
		
		
		add(criarClienteRoute);
		add(calcularFreqRoute);
		add(alterarLocalizacaoRoute);

	}

	 
}
