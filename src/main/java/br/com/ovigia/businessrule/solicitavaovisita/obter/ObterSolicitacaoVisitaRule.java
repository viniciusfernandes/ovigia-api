package br.com.ovigia.businessrule.solicitavaovisita.obter;

import java.util.ArrayList;
import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataHora;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

public class ObterSolicitacaoVisitaRule
		implements BusinessRule<ObterSolicitacaoVisitaRequest, List<ObterSolicitacaoVisitaResponse>> {
	private SolicitacaoVisitaRepository solicitacaoRepository;

	public ObterSolicitacaoVisitaRule(SolicitacaoVisitaRepository solicitacaoRepository) {
		this.solicitacaoRepository = solicitacaoRepository;
	}

	@Override
	public Mono<Response<List<ObterSolicitacaoVisitaResponse>>> apply(ObterSolicitacaoVisitaRequest request) {
		return solicitacaoRepository.obterSolicitacaoByIdVigia(request.idVigia)

				.collectList().map(solicitacoes -> {
					var response = new ArrayList<ObterSolicitacaoVisitaResponse>();
					ObterSolicitacaoVisitaResponse solResponse = null;
					DataHora dataHora = null;
					for (var sol : solicitacoes) {
						solResponse = new ObterSolicitacaoVisitaResponse();
						dataHora = DataUtil.gerarDataHora(sol.data);
						solResponse.data = dataHora.data;
						solResponse.hora = dataHora.hora;
						solResponse.idCliente = sol.idCliente;
						solResponse.localizacaoCliente = sol.localizacaoCliente;
						solResponse.nomeCliente = sol.nomeCliente;
						solResponse.telefoneCliente = sol.telefoneCliente;
						response.add(solResponse);
					}
					return Response.ok(response);
				});
	}
}
