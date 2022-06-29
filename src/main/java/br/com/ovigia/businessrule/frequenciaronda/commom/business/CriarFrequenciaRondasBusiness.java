package br.com.ovigia.businessrule.frequenciaronda.commom.business;

import java.util.Date;
import java.util.List;

import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.IdFrequenciaRonda;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.ResumoFrequenciaRonda;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.ResumoFrequenciaRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class CriarFrequenciaRondasBusiness {
	// Distancia em metros
	private final double distanciaMinima = 0.02d;
	// INTERVALO DE TOLERANCIA EM MILISEGUNDOS PARA TERMINAR A FREQUENCIA DA RONDA E
	// ELIMINAR O CENARIO EM QUE O VIGIA ESTA PARADO EM FRENTE A PROPRIEDADE DO
	// CLIENTE EM 5 MINUTOS
	private final long tolerancia = 5 * 60 * 1000;
	private ClienteRepository clienteRepository;

	private CalculadoraDistancia calculadoraDistancia = CalculadoraDistancia.calculadoraEsferica();
	private RondaRepository rondaRepository;
	private ResumoFrequenciaRondaRepository frequenciaRepository;
	private VigiaRepository vigiaRepository;

	public CriarFrequenciaRondasBusiness(ClienteRepository clienteRepository, RondaRepository rondaRepository,
			ResumoFrequenciaRondaRepository frequenciaRepository, VigiaRepository vigiaRepository) {
		this.clienteRepository = clienteRepository;
		this.rondaRepository = rondaRepository;
		this.frequenciaRepository = frequenciaRepository;
		this.vigiaRepository = vigiaRepository;
	}

	public Mono<FrequenciaRonda> apply(String idCliente, Date dataAtualiacaoRonda) {
		//comentario teste
		return clienteRepository.obterIdVigiaELocalizacaoByIdCliente(idCliente).flatMap(cliente -> {
			var idVigia = cliente.idVigia;
			var localizacaoCliente = cliente.localizacao;
			return vigiaRepository.obterDataUltimaRonda(idVigia).flatMap(vigia -> {
				var dataUltimaRonda = vigia.dataUltimaRonda;
				var idUltimaRonda = new IdRonda(idVigia, dataUltimaRonda);
				return calcularFrequenciaRonda(idUltimaRonda, localizacaoCliente).map(totalRonda -> {
					var frequecia = new FrequenciaRonda();
					frequecia.dataUltimaRonda = idUltimaRonda.dataRonda;
					frequecia.idVigia = idUltimaRonda.idVigia;
					frequecia.totalRonda = totalRonda;
					frequecia.dataAtualizacaoRonda = dataAtualiacaoRonda;
					return frequecia;
				});
			});
		}).flatMap(frequencia -> clienteRepository.atualizarFrequenciaRonda(idCliente, frequencia)).map(frequencia -> {
			var resumo = new ResumoFrequenciaRonda();
			resumo.id = new IdFrequenciaRonda(idCliente, frequencia.dataUltimaRonda);
			resumo.totalRonda = frequencia.totalRonda;
			resumo.idVigia = frequencia.idVigia;
			// return frequenciaRepository.criarResumo(resumo).thenReturn(frequencia);
			return frequencia;
		});
	}

	private int calcularTotalRondasCliente(Localizacao localizacaoCliente, List<Localizacao> localizacoesVigia) {
		if (localizacoesVigia == null || localizacoesVigia.isEmpty()) {
			return 0;
		}

		// Ordenando a lista por horario de inclusao para poder determinar a data
		// de inicio de ronda em frente a propriedade do cliente
		localizacoesVigia.sort((loc1, loc2) -> loc1.timestamp.compareTo(loc2.timestamp));

		int totalRondas = 0;
		Long dataInicio = null;
		boolean isDistanciaOK = false;
		// AQUI ESTAMOS CONSIDERANDO QUE AS ROTAS ESTAO ORDENADAS PELA DATA E HORA DE
		// INCLUSAO
		for (var locVigia : localizacoesVigia) {
			isDistanciaOK = calculadoraDistancia.isDistanciaOk(locVigia, localizacaoCliente, distanciaMinima);
			if (isDistanciaOK && (dataInicio == null || isDataForaIntervalo(dataInicio, locVigia.timestamp))) {
				dataInicio = locVigia.timestamp;
				totalRondas++;
			}
		}
		return totalRondas;
	}

	private Mono<Integer> calcularFrequenciaRonda(IdRonda idUltimaRonda, Localizacao localizacaoCliente) {
		return rondaRepository.obterRondaPorId(idUltimaRonda)
				.map(ronda -> calcularTotalRondasCliente(localizacaoCliente, ronda.localizacoes));
	}

	private boolean isDataForaIntervalo(long dataAntes, long dataDepois) {
		return Math.abs(dataAntes - dataDepois) >= tolerancia;
	}
}
