package br.com.ovigia.businessrule.contrato.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class ObterContratoAtivoClienteRule
		implements BusinessRule<ObterContratoAtivoClienteRequest, ObterContratoAtivoClienteResponse> {
	private ContratoRepository contratoRepository;
	private VigiaRepository vigiaRepository;

	public ObterContratoAtivoClienteRule(ContratoRepository contratoRepository, VigiaRepository vigiaRepository) {
		this.contratoRepository = contratoRepository;
		this.vigiaRepository = vigiaRepository;
	}

	@Override
	public Mono<Response<ObterContratoAtivoClienteResponse>> apply(ObterContratoAtivoClienteRequest request) {
		return contratoRepository.obterContratoAtivoByIdCliente(request.idCliente).map(contrato -> {
			var response = new ObterContratoAtivoClienteResponse();
			response.id = contrato.id;
			response.dataInicio = DataUtil.formatarData(contrato.dataInicio);
			response.dataVencimento = DataUtil.formatarData(contrato.dataVencimento);
			response.valor = contrato.valor;
			response.idVigia = contrato.idVigia;
			response.nomeVigia = contrato.nomeCliente;
			response.isVencido = contrato.isContratoVencido();

			return response;
		}).flatMap(response -> {
			return vigiaRepository.obterNomeETelefoneEAvaliacao(response.idVigia).map(vigia -> {
				response.nomeVigia = vigia.nome;
				response.avaliacaoVigia = vigia.avaliacao == null ? 0d : vigia.avaliacao;
				response.telefoneVigia = vigia.formatarTelefone();
				return response;
			});
		}).map(Response::ok).switchIfEmpty(Mono.just(Response.noContent()));
	}

}
