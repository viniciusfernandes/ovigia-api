package br.com.ovigia.businessrule.solicitavaovisita.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.resquest.common.LocalizacaoResponse;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterVigiaSolicitadoRule
		implements BusinessRule<ObterVigiaSolicitadoRequest, ObterVigiaSolicitadoResponse> {
	private SolicitacaoVisitaRepository solicitacaoRepository;
	private VigiaRepository vigiaRepository;

	public ObterVigiaSolicitadoRule(SolicitacaoVisitaRepository solicitacaoRepository,
			VigiaRepository vigiaRepository) {
		this.solicitacaoRepository = solicitacaoRepository;
		this.vigiaRepository = vigiaRepository;
	}

	@Override
	public Mono<Response<ObterVigiaSolicitadoResponse>> apply(ObterVigiaSolicitadoRequest request) {
		return solicitacaoRepository.obterIdVigiaSolicitado(request.idCliente)
				.flatMap(idVigia -> vigiaRepository.obterVigiaPorId(idVigia)).map(vigia -> {
					var response = new ObterVigiaSolicitadoResponse();
					response.id = vigia.id;
					response.nome = vigia.nome;
					response.dataInicio = DataUtil.formatarData(vigia.dataInicio);
					response.avaliacao = vigia.avaliacao == null ? 0.00 : vigia.avaliacao.valor;
					response.telefone = vigia.formatarTelefone();
					if (vigia.localizacao != null) {
						var loc = new LocalizacaoResponse();
						loc.latitude = vigia.localizacao.latitude;
						loc.latitude = vigia.localizacao.latitude;
						response.localizacao = loc;
					}
					return Response.ok(response);
				});
	}
}
