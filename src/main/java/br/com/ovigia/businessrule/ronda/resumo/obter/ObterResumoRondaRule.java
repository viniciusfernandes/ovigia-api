package br.com.ovigia.businessrule.ronda.resumo.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.repository.ChamadoRepository;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterResumoRondaRule implements BusinessRule<ObterResumoRondaRequest, ObterResumoRondaResponse> {
	private ResumoRondaRepository resumoRepository;
	private ChamadoRepository chamadoRepository;
	private VigiaRepository vigiaRepository;

	public ObterResumoRondaRule(ResumoRondaRepository resumoRepository, ChamadoRepository chamadoRepository,
			VigiaRepository vigiaRepository) {
		this.resumoRepository = resumoRepository;
		this.chamadoRepository = chamadoRepository;
		this.vigiaRepository = vigiaRepository;
	}

	@Override
	public Mono<Response<ObterResumoRondaResponse>> apply(ObterResumoRondaRequest request) {
		return vigiaRepository.obterDataUltimaRonda(request.idVigia).flatMap(vigia -> {
			var id = new IdRonda(request.idVigia, vigia.dataUltimaRonda);
			return Mono.zip(resumoRepository.obterResumoRondaById(id),
					chamadoRepository.obterTotalChamadoAceitoByIdRonda(id)).map(tuple -> {
						var resumo = tuple.getT1();
						var totalChamados = tuple.getT2();
						var tempoEscala = CalculadoraDistancia.calcularTempoEscala(resumo.tempo);

						var response = new ObterResumoRondaResponse();
						response.distancia = resumo.distancia;
						response.escalaTempo = tempoEscala.escala;
						response.tempo = tempoEscala.tempo;
						response.totalChamados = totalChamados;

						var dataHora = resumo.id == null ? DataUtil.obterDataHora()
								: DataUtil.obterDataHora(resumo.id.dataRonda);
						response.data = dataHora.data;
						response.hora = dataHora.hora;
						return Response.ok(response);
					});
		}).switchIfEmpty(Mono.just(Response.ok(new ObterResumoRondaResponse().inicializar())));

	}

}
