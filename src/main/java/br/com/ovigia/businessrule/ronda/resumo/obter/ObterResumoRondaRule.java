package br.com.ovigia.businessrule.ronda.resumo.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.repository.ChamadoRepository;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import reactor.core.publisher.Mono;

public class ObterResumoRondaRule implements BusinessRule<ObterResumoRondaRequest, ObterResumoRondaResponse> {
	private ResumoRondaRepository resumoRepository;
	private ChamadoRepository chamadoRepository;

	public ObterResumoRondaRule(ResumoRondaRepository resumoRepository, ChamadoRepository chamadoRepository) {
		this.resumoRepository = resumoRepository;
		this.chamadoRepository = chamadoRepository;
	}

	@Override
	public Mono<Response<ObterResumoRondaResponse>> apply(ObterResumoRondaRequest request) {

		return Mono.from(resumoRepository.obterResumoRondaByIdVigia(request.idVigia)).flatMap(resumo -> {
			var idRonda = new IdRonda(resumo.idVigia, resumo.data);
			return chamadoRepository.obterTotalChamadoAceitoByIdRonda(idRonda).map(totalChamados -> {
				resumo.totalChamados = totalChamados;
				return resumo;
			});
		}).map(resumo -> {
			var response = new ObterResumoRondaResponse();
			response.distancia = resumo.distancia;
			response.escalaTempo = resumo.escalaTempo;
			response.tempo = resumo.tempo;
			response.totalChamados = resumo.totalChamados;

			var dataHora = DataUtil.obterDataHora(resumo.data);
			response.data = dataHora.data;
			response.hora = dataHora.hora;
			return Response.ok(response);
		});
	}

}
