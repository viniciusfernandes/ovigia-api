package br.com.ovigia.businessrule.contrato.criar;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.enumeration.TipoSituacaoContrato;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

public class CriarContratoRule implements BusinessRule<CriarContratoRequest, CriarContratoResponse> {
	private ContratoRepository contratoRepository;
	private SolicitacaoVisitaRepository solicitacaoVisitaRepository;

	public CriarContratoRule(ContratoRepository contratoRepository,
			SolicitacaoVisitaRepository solicitacaoVisitaRepository) {
		this.contratoRepository = contratoRepository;
		this.solicitacaoVisitaRepository = solicitacaoVisitaRepository;
	}

	@Override
	public Mono<Response<CriarContratoResponse>> apply(CriarContratoRequest request) {
		var contrato = new Contrato();
		contrato.dataInicio = new Date();
		contrato.dataVencimento = DataUtil.ajustarDataProximoMes();
		contrato.idCliente = request.idCliente;
		contrato.idVigia = request.idVigia;
		contrato.valor = request.valor;
		contrato.nomeCliente = request.nomeCliente;
		contrato.telefoneCliente = request.telefoneCliente;
		contrato.situacao = TipoSituacaoContrato.ATIVO;
		return contratoRepository.criarContrato(contrato).flatMap(idContrato -> solicitacaoVisitaRepository
				.removerSolicitacao(contrato.idCliente).thenReturn(Response.ok(new CriarContratoResponse(idContrato))));
	}

}
