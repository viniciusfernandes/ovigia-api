package br.com.ovigia.businessrule;

import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Mensalidade;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.MensalidadeRepository;

public class CriarMensalidadesTask {
	private MensalidadeRepository mensalidadeRepository;
	private ContratoRepository contratoRepository;
	private Logger logger = LoggerFactory.getLogger(getClass());

	public CriarMensalidadesTask(ContratoRepository contratoRepository, MensalidadeRepository mensalidadeRepository) {
		this.mensalidadeRepository = mensalidadeRepository;
		this.contratoRepository = contratoRepository;
	}

	public void runTask() throws Exception {
		var timer = new Timer("CriarMensalidadesTimerTask");
		long delay = 1000L;
		var task = new TimerTask() {
			public void run() {
				criarMensalidades();
			}
		};
		timer.schedule(task, delay, 20000);
	}

	private void criarMensalidades() {
		contratoRepository.obterIdContratosVencidos().flatMap(contrato -> {
			var mensalidade = new Mensalidade();
			mensalidade.dataVencimento = contrato.dataVencimento;
			mensalidade.idContrato = contrato.id;
			mensalidade.nomeCliente = contrato.nomeCliente;
			mensalidade.telefoneCliente = contrato.telefoneCliente;
			mensalidade.valor = contrato.valor;
			mensalidade.idVigia = contrato.idVigia;
			return mensalidadeRepository.criarMensalidade(mensalidade);

		}).map(mensalidade -> {
			var dataVencimento = DataUtil.ajustarDataProximoMes(mensalidade.dataVencimento);
			return contratoRepository.atualizarDataVencimentoContrato(mensalidade.idContrato, dataVencimento);
		}).doOnError(ex -> logger.error("Falha ao criar as mensalidades")).subscribe();
	}
//	private void criarMensalidades() {
//		contratoRepository.obterIdContratosVencidos().map(contrato -> {
//
//			var dataVencimento = DataUtil.ajustarDataProximoMes(contrato.dataVencimento);
//			contrato.dataVencimento = dataVencimento;
//			return contratoRepository.atualizarDataFimContrato(contrato.id, dataVencimento);
//
//		}).doOnError(ex -> logger.error("Falha ao criar as mensalidades")).blockFirst();
//	}
}
