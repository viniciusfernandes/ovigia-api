package br.com.ovigia.businessrule.solicitavaovisita.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataHora;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

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
					ObterSolicitacaoVisitaResponse solicitacaoResponse = null;
					DataHora dataHora = null;
					for (var sol : solicitacoes) {
						solicitacaoResponse = new ObterSolicitacaoVisitaResponse();
						dataHora = DataUtil.obterDataHora(sol.data);
						solicitacaoResponse.data = dataHora.data;
						solicitacaoResponse.hora = dataHora.hora;
						solicitacaoResponse.idCliente = sol.idCliente;
						solicitacaoResponse.localizacaoCliente = sol.localizacaoCliente;
						solicitacaoResponse.nomeCliente = sol.nomeCliente;
						solicitacaoResponse.telefoneCliente = sol.telefoneCliente;
						response.add(solicitacaoResponse);
					}
					return Response.ok(response);
				});
	}
}
