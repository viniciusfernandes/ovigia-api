package br.com.ovigia.businessrule.ronda.criar;

import static br.com.ovigia.businessrule.util.DataUtil.gerarData;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Id;
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
		ronda.id = new Id(request.idVigia, gerarData());
		ronda.localizacoes = request.localizacoes;
		ronda.fim = request.fim;
		ronda.inicio = request.inicio;

		return repository.contemRonda(ronda.id).flatMap(contem -> {
			Mono<Void> mono = null;
			if (contem) {
				mono = repository.atualizarLocalizacoes(ronda);
			} else {
				mono = repository.criar(ronda);
			}

			return mono.thenReturn(Response.ok(gerarResponse(ronda)));
		});
	}

	private CriarRondaResponse gerarResponse(Ronda ronda) {
		var distancia = calculadoraDistancia.calcularDistancia(ronda);
		var tempoEscala = calculadoraDistancia.calcularTempo(ronda);
		return new CriarRondaResponse(distancia, tempoEscala.tempo, tempoEscala.escala);
	}

}
