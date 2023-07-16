package br.com.ovigia.businessrule;

import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Mensalidade;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.MensalidadeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Timer;
import java.util.TimerTask;

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
		timer.schedule(task, delay, 2000);
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
			mensalidade.situacao = TipoSituacaoMensalidade.ABERTO;
			return mensalidadeRepository.criarMensalidade(mensalidade);

		}).doOnNext(mensalidade -> {
			var dataVencimento = DataUtil.ajustarDataProximoMes(mensalidade.dataVencimento);
			contratoRepository.atualizarDataVencimentoContrato(mensalidade.idContrato, dataVencimento)
					.doOnError(ex -> logger.error("Falha ao atualizar a data de vencimento=" + dataVencimento
							+ " do contrato=" + mensalidade.idContrato))
					.subscribe();
		}).doOnError(ex -> logger.error("Falha ao criar as mensalidades")).subscribe();
	}
}
