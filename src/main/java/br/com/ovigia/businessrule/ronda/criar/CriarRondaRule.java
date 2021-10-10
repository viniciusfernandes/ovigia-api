package br.com.ovigia.businessrule.ronda.criar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Mono;

public class CriarRondaRule implements BusinessRule<CriarRondaRequest, CriarRondaResponse> {

	private RondaRepository repository;
	private CalculadoraDistancia calculadoraDistancia = CalculadoraDistancia.calculadoraEsferica();

	public CriarRondaRule(RondaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<CriarRondaResponse>> apply(CriarRondaRequest request) {
		var ronda = new Ronda();
		ronda.id = new IdRonda(request.idVigia, request.data);
		ronda.localizacoes = request.localizacoes;

		var response = new CriarRondaResponse(calculadoraDistancia.calcularDistancia(ronda));
		return repository.criar(ronda).thenReturn(Response.ok(response));
	}

}
