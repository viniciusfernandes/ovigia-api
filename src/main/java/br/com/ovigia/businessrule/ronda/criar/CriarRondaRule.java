package br.com.ovigia.businessrule.ronda.criar;

import static br.com.ovigia.businessrule.util.DataUtil.gerarData;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Id;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.enumeration.TipoSituacaoRonda;
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
		ronda.situacao = TipoSituacaoRonda.ATIVO;
		ronda.distancia = calculadoraDistancia.calcularDistancia(ronda);

		return repository.obterDistanciaRonda(ronda.id).flatMap(distancia -> {
			ronda.distancia += distancia;
			return repository.concatenarRonda(ronda).thenReturn(ronda);
		}).switchIfEmpty(repository.criarRonda(ronda).thenReturn(ronda)).map(r -> Response.ok(gerarResponse(ronda)));

	}

	private CriarRondaResponse gerarResponse(Ronda ronda) {
		var tempoEscala = calculadoraDistancia.calcularTempo(ronda);
		return new CriarRondaResponse(ronda.distancia, tempoEscala.tempo, tempoEscala.escala);
	}

}
