package br.com.ovigia.businessrule.ronda.criar;

import static br.com.ovigia.businessrule.util.DataUtil.ajustarData;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.ResumoRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.calculadora.CalculadoraRonda;
import br.com.ovigia.model.enumeration.TipoSituacaoRonda;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Mono;

public class CriarRondaRule implements BusinessRule<CriarRondaRequest, Void> {

	private RondaRepository rondaRepository;
	private ResumoRondaRepository resumoRepository;

	private CalculadoraRonda calculadora = CalculadoraRonda.calculadoraEsferica();

	public CriarRondaRule(RondaRepository rondaRepository, ResumoRondaRepository resumoRepository) {
		this.rondaRepository = rondaRepository;
		this.resumoRepository = resumoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CriarRondaRequest request) {
		final var ronda = new Ronda();
		ronda.id = new IdRonda(request.idVigia, ajustarData());
		ronda.localizacoes = request.localizacoes;
		ronda.fim = request.fim;
		ronda.inicio = request.inicio;
		ronda.situacao = TipoSituacaoRonda.ATIVO;
		return resumoRepository.obterResumoRondaByIdVigia(ronda.id.idVigia).flatMap(resumo -> {
			if (resumo.idVigia != null) {
				return concatenaRondaEResumo(ronda, resumo);
			}
			return criarRondaEResumo(ronda);
		}).thenReturn(Response.noContent());
	}

	private Mono<Void> concatenaRondaEResumo(Ronda ronda, ResumoRonda resumo) {
		var tempoEscala = calculadora.calcularTempo(ronda);
		resumo.escalaTempo = tempoEscala.escala;
		resumo.tempo += tempoEscala.tempo;
		resumo.distancia += calculadora.calcularDistancia(ronda);
		resumo.totalChamados += 0l;
		return rondaRepository.concatenarRonda(ronda).and(resumoRepository.removerResumoRonda(ronda.id.idVigia))
				.and(resumoRepository.criarResumoRonda(resumo));

	}

	private Mono<Void> criarRondaEResumo(Ronda ronda) {
		var resumo = gerarResumo(ronda);
		return rondaRepository.criarRonda(ronda).and(resumoRepository.removerResumoRonda(ronda.id.idVigia))
				.and(resumoRepository.criarResumoRonda(resumo));

	}

	private ResumoRonda gerarResumo(Ronda ronda) {
		var resumo = new ResumoRonda();
		resumo.idVigia = ronda.id.idVigia;
		resumo.data = ronda.fim;
		var tempoEscala = calculadora.calcularTempo(ronda);
		resumo.distancia = calculadora.calcularDistancia(ronda);
		resumo.escalaTempo = tempoEscala.escala;
		resumo.tempo = tempoEscala.tempo;
		resumo.totalChamados = 0l;
		return resumo;
	}

}
