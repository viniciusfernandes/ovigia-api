package br.com.ovigia.businessrule.ronda.resumo.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import reactor.core.publisher.Mono;

public class ObterResumoRondaRule implements BusinessRule<ObterResumoRondaRequest, ObterResumoRondaResponse> {
	private ResumoRondaRepository resumoRepository;

	public ObterResumoRondaRule(ResumoRondaRepository resumoRepository) {
		this.resumoRepository = resumoRepository;
	}

	@Override
	public Mono<Response<ObterResumoRondaResponse>> apply(ObterResumoRondaRequest request) {
		return Mono.from(resumoRepository.obterResumoRondaByIdVigia(request.idVigia)).flatMap(resumo -> {
			var response = new ObterResumoRondaResponse();
			response.distancia = resumo.distancia;
			response.escalaTempo = resumo.escalaTempo;
			response.tempo = resumo.tempo;
			response.totalChamados = resumo.totalChamados;

			var dataHora = DataUtil.gerarDataHora(resumo.data);
			response.data = dataHora.data;
			response.hora = dataHora.hora;
			return Mono.just(Response.ok(response));
		}).switchIfEmpty(Mono.just(Response.<ObterResumoRondaResponse>nonResult()));
	}

}
