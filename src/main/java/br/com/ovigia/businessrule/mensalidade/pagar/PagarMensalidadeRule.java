package br.com.ovigia.businessrule.mensalidade.pagar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.businessrule.util.MesAno;
import br.com.ovigia.model.Faturamento;
import br.com.ovigia.model.IdFaturamento;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.FaturamentoRepository;
import br.com.ovigia.model.repository.MensalidadeRepository;
import reactor.core.publisher.Mono;

import java.util.Date;

public class PagarMensalidadeRule implements BusinessRule<PagarMensalidadeRequest, Void> {
	private MensalidadeRepository mensalidadeRepository;
	private FaturamentoRepository faturamentoRepository;

	public PagarMensalidadeRule(MensalidadeRepository mensalidadeRepository,
			FaturamentoRepository faturamentoRepository) {
		this.mensalidadeRepository = mensalidadeRepository;
		this.faturamentoRepository = faturamentoRepository;
	}

	@Override
	public Mono<Response<Void>> apply(PagarMensalidadeRequest request) {
		var dataAtual = new Date();
		var mesAno = DataUtil.obterMesAno(dataAtual);
		return mensalidadeRepository
				.atualizaDataPagamentoMensalidade(request.idMensalidade, dataAtual, TipoSituacaoMensalidade.PAGO)
				.and(atualizarValorFaturamento(request.idVigia, mesAno, request.valor))
				.thenReturn(Response.noContent());
	}

	private Mono<Faturamento> atualizarValorFaturamento(String idvigia, MesAno mesAno, Double valor) {
		var id = new IdFaturamento(idvigia, mesAno.mes, mesAno.ano);
		return faturamentoRepository.obterValoresFaturamento(id).flatMap(faturamento -> {
			faturamento.id = id;
			faturamento.valor = faturamento.valor + valor;
			faturamento.quantidadePagamentos = faturamento.quantidadePagamentos + 1;
			return faturamentoRepository.atualizarFaturamento(faturamento).thenReturn(faturamento);
		}).switchIfEmpty(faturamentoRepository.criarFaturamento(new Faturamento(id, valor)));
	}

}
