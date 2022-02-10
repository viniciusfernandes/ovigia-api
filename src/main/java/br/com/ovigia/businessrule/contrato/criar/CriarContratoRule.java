package br.com.ovigia.businessrule.contrato.criar;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.enumeration.TipoSituacaoContrato;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

public class CriarContratoRule implements BusinessRule<CriarContratoRequest, CriarContratoResponse> {
	private ContratoRepository contratoRepository;
	private SolicitacaoVisitaRepository solicitacaoVisitaRepository;
	private ClienteRepository clienteRepository;

	public CriarContratoRule(ContratoRepository contratoRepository,
			SolicitacaoVisitaRepository solicitacaoVisitaRepository, ClienteRepository clienteRepository) {
		this.contratoRepository = contratoRepository;
		this.solicitacaoVisitaRepository = solicitacaoVisitaRepository;
		this.clienteRepository = clienteRepository;
	}

	@Override
	public Mono<Response<CriarContratoResponse>> apply(CriarContratoRequest request) {
		var contrato = new Contrato();
		contrato.dataInicio = new Date();
		contrato.dataVencimento = DataUtil.ajustarDataProximoMes();
		contrato.idCliente = request.idCliente;
		contrato.idVigia = request.idVigia;
		contrato.valor = request.valor;
		if (contrato.valor == null) {
			contrato.valor = 0d;
		}
		contrato.nomeCliente = request.nomeCliente;
		contrato.telefoneCliente = request.telefoneCliente;
		contrato.situacao = TipoSituacaoContrato.ATIVO;
		return contratoRepository.criarContrato(contrato).flatMap(
				idContrato -> solicitacaoVisitaRepository.removerSolicitacao(contrato.idCliente).thenReturn(idContrato))
				.flatMap(idContrato -> clienteRepository.atualizarIdVigia(request.idVigia, request.idCliente)
						.thenReturn(idContrato))
				.map(idContrato -> Response.ok(new CriarContratoResponse(idContrato)));
	}

}
