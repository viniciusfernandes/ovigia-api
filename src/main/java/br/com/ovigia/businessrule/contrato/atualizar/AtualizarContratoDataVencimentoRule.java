package br.com.ovigia.businessrule.contrato.atualizar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

public class AtualizarContratoDataVencimentoRule
		implements BusinessRule<AtualizarContratoDataVencimentoResquest, Void> {
private ContratoRepository contratoRepository;
	@Override
	public Mono<Response<Void>> apply(AtualizarContratoDataVencimentoResquest request) {
		// TODO Auto-generated method stub
		return null;
	}

}
