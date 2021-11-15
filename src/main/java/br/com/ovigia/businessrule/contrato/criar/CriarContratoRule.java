package br.com.ovigia.businessrule.contrato.criar;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

public class CriarContratoRule implements BusinessRule<CriarContratoRequest, Void> {
	private ContratoRepository contratoRepository;

	public CriarContratoRule(ContratoRepository contratoRepository) {
		this.contratoRepository = contratoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CriarContratoRequest request) {
		var contrato = new Contrato();
		contrato.dataInicio = new Date();
		contrato.idCliente = request.idCliente;
		contrato.idVigia = request.idVigia;
		contrato.valor = request.valor;
		return contratoRepository.criarContrato(contrato).thenReturn(Response.noContent());
	}
}
