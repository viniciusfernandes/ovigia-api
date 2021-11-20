package br.com.ovigia.businessrule.contrato.criar;

import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Mono;

public class CriarContratoRule implements BusinessRule<CriarContratoRequest, Void> {
	private ContratoRepository contratoRepository;
	private SolicitacaoVisitaRepository solicitacaoVisitaRepository;
	private ClienteRepository clienteRepository;

	public CriarContratoRule(ClienteRepository clienteRepository, ContratoRepository contratoRepository,
			SolicitacaoVisitaRepository solicitacaoVisitaRepository) {
		this.clienteRepository = clienteRepository;
		this.contratoRepository = contratoRepository;
		this.solicitacaoVisitaRepository = solicitacaoVisitaRepository;
	}

	@Override
	public Mono<Response<Void>> apply(CriarContratoRequest request) {
		var contrato = new Contrato();
		contrato.dataInicio = new Date();
		var diaMes = DataUtil.obterDiaMes(contrato.dataInicio);
		contrato.diaVencimento = diaMes.dia;
		contrato.mesVencimento = diaMes.mes;
		contrato.idCliente = request.idCliente;
		contrato.idVigia = request.idVigia;
		contrato.valor = request.valor;
		contrato.nomeCliente = request.nomeCliente;
		contrato.telefoneCliente = request.telefoneCliente;
		return contratoRepository.criarContrato(contrato)
				.and(solicitacaoVisitaRepository.removerSolicitacao(request.idCliente))
				.thenReturn(Response.noContent());
	}

}
