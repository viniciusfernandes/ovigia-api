package br.com.ovigia.businessrule.mensalidade.obter;

import java.util.Date;
import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.MensalidadeRepository;
import reactor.core.publisher.Mono;

public class ObterMensalidadesVencidasRule
		implements BusinessRule<ObterMensalidadesVencidasRequest, List<ObterMensalidadesVencidasResponse>> {
	private MensalidadeRepository mensalidadeRepository;

	public ObterMensalidadesVencidasRule(MensalidadeRepository mensalidadeRepository) {
		this.mensalidadeRepository = mensalidadeRepository;
	}

	@Override
	public Mono<Response<List<ObterMensalidadesVencidasResponse>>> apply(ObterMensalidadesVencidasRequest request) {
		return mensalidadeRepository.obterMensalidadesDataVencimentoInferiorByIdVigia(request.idVigia, new Date(),
				TipoSituacaoMensalidade.ABERTO).map(mensalidade -> {
					var response = new ObterMensalidadesVencidasResponse();
					response.dataVencimento = DataUtil.formatarData(mensalidade.dataVencimento);
					response.id = mensalidade.id;
					response.nomeCliente = mensalidade.nomeCliente;
					response.telefoneCliente = mensalidade.telefoneCliente;
					response.valor = mensalidade.valor;
					return response;
				}).collectList().map(Response::ok);
	}

}
