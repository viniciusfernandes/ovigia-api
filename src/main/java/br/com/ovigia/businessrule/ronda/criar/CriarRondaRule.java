package br.com.ovigia.businessrule.ronda.criar;

import static br.com.ovigia.businessrule.util.DataUtil.ajustarData;
import static br.com.ovigia.model.calculadora.CalculadoraDistancia.calcularTempo;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.ResumoRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.enumeration.TipoSituacaoRonda;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class CriarRondaRule implements BusinessRule<CriarRondaRequest, Void> {

	private RondaRepository rondaRepository;
	private ResumoRondaRepository resumoRepository;
	private VigiaRepository vigiaRepository;
	private CalculadoraDistancia calculadora = CalculadoraDistancia.calculadoraEsferica();

	public CriarRondaRule(RondaRepository rondaRepository, ResumoRondaRepository resumoRepository,
			VigiaRepository vigiaRepository) {
		this.rondaRepository = rondaRepository;
		this.resumoRepository = resumoRepository;
		this.vigiaRepository = vigiaRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CriarRondaRequest request) {
		final var ronda = new Ronda();
		var idRonda = new IdRonda(request.idVigia, ajustarData());
		ronda.id = idRonda;
		ronda.localizacoes = request.localizacoes;
		ronda.fim = request.fim;
		ronda.inicio = request.inicio;
		ronda.dataAtualizacao = new Date();
		ronda.situacao = TipoSituacaoRonda.ATIVO;
		return vigiaRepository.atualizarDataUltimaRonda(idRonda.idVigia, idRonda.dataRonda, ronda.dataAtualizacao)
				.flatMap(result -> {
					return resumoRepository.obterResumoRondaById(idRonda).flatMap(resumo -> {
						Mono<ResumoRonda> mono = null;
						if (resumo.id != null) {
							mono = concatenaRondaEResumo(ronda, resumo);
						} else {
							mono = criarRondaEResumo(ronda);
						}
						return mono.thenReturn(Response.noContent());
					});
				});

	}

	private Mono<ResumoRonda> concatenaRondaEResumo(Ronda ronda, ResumoRonda resumo) {
		resumo.tempo += calcularTempo(ronda);
		resumo.distancia += calculadora.calcularDistancia(ronda);
		resumo.totalChamados += 0l;
		return rondaRepository.concatenarRonda(ronda).thenReturn(resumo)
				.flatMap(result -> resumoRepository.atualizarResumoRonda(resumo).thenReturn(resumo));

	}

	private Mono<ResumoRonda> criarRondaEResumo(Ronda ronda) {
		var resumo = gerarResumo(ronda);
		return rondaRepository.criarRonda(ronda).and(resumoRepository.criarResumoRonda(resumo)).thenReturn(resumo);

	}

	private ResumoRonda gerarResumo(Ronda ronda) {
		var resumo = new ResumoRonda();
		resumo.id = ronda.id;
		resumo.distancia = calculadora.calcularDistancia(ronda);
		resumo.tempo = calcularTempo(ronda);
		resumo.totalChamados = 0l;
		return resumo;
	}

}
