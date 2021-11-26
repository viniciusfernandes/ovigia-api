package br.com.ovigia.businessrule.contrato.cancelar;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

public class CancelarContratoRule implements BusinessRule<CancelarContratoRequest, Void> {
	private ContratoRepository contratoRepository;

	public CancelarContratoRule(ContratoRepository contratoRepository) {
		this.contratoRepository = contratoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CancelarContratoRequest request) {
		return contratoRepository.atualizarDataFimContrato(request.idContrato, new Date())
				.thenReturn(Response.noContent());
	}

}
