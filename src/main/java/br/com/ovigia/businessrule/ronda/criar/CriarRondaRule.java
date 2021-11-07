package br.com.ovigia.businessrule.ronda.criar;

import static br.com.ovigia.businessrule.util.DataUtil.gerarData;

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
		ronda.id = new IdRonda(request.idVigia, gerarData());
		ronda.localizacoes = request.localizacoes;
		ronda.fim = request.fim;
		ronda.inicio = request.inicio;
		ronda.situacao = TipoSituacaoRonda.ATIVO;
		return resumoRepository.obterResumoRondaByIdVigia(ronda.id.idVigia)
				.flatMap(resumo -> {
					if(resumo.idVigia!=null) {
						return concatenaRondaEResumo(ronda, resumo);
					}
					return criarRondaEResumo(ronda) ;
				}) 
				.thenReturn(Response.nonResult());
	}

	private Mono<Void> concatenaRondaEResumo(Ronda ronda, ResumoRonda resumo) {
		System.out.println("Concatenou ronda");
		var tempoEscala = calculadora.calcularTempo(ronda);
		resumo.escalaTempo = tempoEscala.escala;
		resumo.tempo += tempoEscala.tempo;
		resumo.distancia += calculadora.calcularDistancia(ronda);
		resumo.totalChamados += 0l;
		return rondaRepository.concatenarRonda(ronda).and(resumoRepository.removerResumoRonda(ronda.id.idVigia))
				.and(resumoRepository.criarResumoRonda(resumo));

	}

	private Mono<Void> criarRondaEResumo(Ronda ronda) {
		System.out.println("Criando a ronda");
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

	public static void main(String[] args) {
		obter().flatMap(r -> {
			if (r.idVigia != null) {
				return concantenar();
			}
			return criar();
		}).subscribe();
//		var mono2 = obter().flatMap(r -> {
//			return concantenar();
//		}).switchIfEmpty(criar()) 	mono2.subscribe();
	}

	public static Mono<ResumoRonda> obter() {
		var value = Math.random() > 0.5;
		// System.out.println("Obtendo: " + value);
		return value ? Mono.just(new ResumoRonda("obteve: " + value, value)) : Mono.<ResumoRonda>empty();
	}

	public static Mono<ResumoRonda> concantenar() {
//		System.out.println("concatenando");
		return Mono.just(new ResumoRonda("concatenou"));

	}

	public static Mono<ResumoRonda> criar() {
//		System.out.println("Criando");
		return Mono.just(new ResumoRonda("criou"));
	}
}
