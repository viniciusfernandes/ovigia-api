package br.com.ovigia.route;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.PATCH;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.cliente.AtualizarClienteRotaRule;
import br.com.ovigia.businessrule.cliente.CalcularFrequenciaRondasRule;
import br.com.ovigia.businessrule.cliente.CalculoFrequencia;
import br.com.ovigia.businessrule.cliente.CriarClienteRule;
import br.com.ovigia.businessrule.exception.DataRotaMalFormatadaException;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.ClienteRepository;
import br.com.ovigia.repository.RotaRepository;
import reactor.core.publisher.Mono;

public class ClienteRoutesBuilder extends RoutesBuilder {

	public ClienteRoutesBuilder(ClienteRepository clienteRepository, RotaRepository rotaRepository) {
		add(route(POST("/ovigia/clientes"), req -> {
			return toBody(handleRequest(req, Cliente.class, new CriarClienteRule(clienteRepository)));
		}));

		add(route(GET("ovigia/clientes/{idCliente}/frequencia-rondas/{data}"), req -> {
			var calculo = new CalculoFrequencia();
			try {
				calculo.setDataRonda(DataUtil.parseToDataRota(req.pathVariable("data")));
				calculo.setIdCliente(req.pathVariable("idCliente"));
				return toBody(
						handleRequest(calculo, new CalcularFrequenciaRondasRule(clienteRepository, rotaRepository)));
			} catch (DataRotaMalFormatadaException e) {
				String mensagem = String.format("A data enviada nao esta no padrao %s. Tente novamente.",
						DataUtil.dataRotaPadrao);
				var mResponse = Mono.just(Response.unprocessable(req.pathVariable("data"), mensagem));
				return toBody(mResponse);
			}
		}));

		add(route(PATCH("ovigia/clientes/{idCliente}/localizacao"), req -> {
			var response = req.bodyToMono(Localizacao.class).map(localizacao -> {
				var cliente = new Cliente();
				cliente.setId(req.pathVariable("idCliente"));
				cliente.setLocalizacao(localizacao);
				return cliente;
			}).flatMap(cliente -> new AtualizarClienteRotaRule(clienteRepository).apply(cliente));

			return toBody(response);
		}));
	}

}
