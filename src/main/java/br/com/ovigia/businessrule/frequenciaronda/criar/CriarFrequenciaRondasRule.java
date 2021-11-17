package br.com.ovigia.businessrule.frequenciaronda.criar;

import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.IdFrequenciaRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.calculadora.CalculadoraRonda;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.FrequenciaRondaRepository;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Mono;

public class CriarFrequenciaRondasRule
		implements BusinessRule<CriarFrequenciaRondaRequest, CriarFrequenciaRondaResponse> {
	// Distancia em metros
	private final double distanciaMinima = 0.02d;
	// INTERVALO DE TOLERANCIA EM MILISEGUNDOS PARA TERMINAR A FREQUENCIA DA RONDA E
	// ELIMINAR O CENARIO EM QUE O VIGIA ESTA PARADO EM FRENTE A PROPRIEDADE DO
	// CLIENTE EM 5 MINUTOS
	private final long tolerancia = 5 * 60 * 1000;
	private ClienteRepository clienteRepository;

	private CalculadoraRonda calculadoraDistancia = CalculadoraRonda.calculadoraEsferica();
	private ContratoRepository contratoRepository;
	private RondaRepository rondaRepository;
	private FrequenciaRondaRepository frequenciaRepository;

	public CriarFrequenciaRondasRule(ClienteRepository clienteRepository, RondaRepository rondaRepository,
			FrequenciaRondaRepository frequenciaRepository, ContratoRepository contratoRepository) {
		this.clienteRepository = clienteRepository;
		this.rondaRepository = rondaRepository;
		this.frequenciaRepository = frequenciaRepository;
		this.contratoRepository = contratoRepository;
	}

	@Override
	public Mono<Response<CriarFrequenciaRondaResponse>> apply(CriarFrequenciaRondaRequest request) {
		var obterIdVigia = contratoRepository.obterIdVigiaByIdCliente(request.idCliente);
		obterIdVigia.subscribe(r -> System.out.println("idvigia: "+r) );
		var obterLocalizacaoCliente = clienteRepository.obterLocalizacaoCliente(request.idCliente);
		obterLocalizacaoCliente .subscribe(r -> System.out.println("obterLocalizacaoCliente : "+r) );

		return Mono.zip(obterIdVigia, obterLocalizacaoCliente).flatMap(tuple -> {
			var idVigia = tuple.getT1();
			var localCliente = tuple.getT2();
			return rondaRepository.obterUltimaDataRonda(idVigia).map(ronda -> {
				var frequecia = new FrequenciaRonda();
				frequecia.id = new IdFrequenciaRonda(request.idCliente, ronda.obterData());
				frequecia.idVigia = idVigia;
				frequecia.totalRonda = calcularNumeroRodas(localCliente, ronda.localizacoes);
				return frequecia;
			});

		}).doOnNext(frequencia -> clienteRepository.atualizarFrequenciaRonda(frequencia))
				.doOnNext(frequencia -> frequenciaRepository.criarFrequenciaRonda(frequencia)).map(frequencia -> {
					var response = new CriarFrequenciaRondaResponse();
					response.data = DataUtil.formatarData(frequencia.id.data);
					response.idVigia = frequencia.idVigia;
					response.totalRonda = frequencia.totalRonda;
					return Response.ok(response);
				});
	}

	private int calcularNumeroRodas(Localizacao localizacaoCliente, List<Localizacao> localizacoesVigia) {
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
