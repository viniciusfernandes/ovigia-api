package br.com.ovigia.businessrule.frequenciaronda.commom.business;

import java.util.List;

import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.IdFrequenciaRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.FrequenciaRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;
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
	private FrequenciaRondaRepository frequenciaRepository;

	public CriarFrequenciaRondasBusiness(ClienteRepository clienteRepository, RondaRepository rondaRepository,
			FrequenciaRondaRepository frequenciaRepository) {
		this.clienteRepository = clienteRepository;
		this.rondaRepository = rondaRepository;
		this.frequenciaRepository = frequenciaRepository;

	}

	public Mono<FrequenciaRonda> apply(String idCliente) {
		return clienteRepository.obterIdVigiaELocalizacaoByIdCliente(idCliente).flatMap(cliente -> {
			var idVigia = cliente.idVigia;
			var localizacao = cliente.localizacao;
			return rondaRepository.obterUltimaRondaByIdVigia(idVigia).map(ronda -> {
				var frequecia = new FrequenciaRonda();
				frequecia.id = new IdFrequenciaRonda(cliente.id, ronda.obterData());
				frequecia.idVigia = idVigia;
				frequecia.totalRonda = calcularTotalRondasCliente(localizacao, ronda.localizacoes);
				return frequecia;
			});

		}).flatMap(frequencia -> clienteRepository.atualizarFrequenciaRonda(frequencia))
				.flatMap(frequencia -> frequenciaRepository.criarFrequenciaRonda(frequencia).thenReturn(frequencia));
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

	private boolean isDataForaIntervalo(long dataAntes, long dataDepois) {
		return Math.abs(dataAntes - dataDepois) >= tolerancia;
	}
}
