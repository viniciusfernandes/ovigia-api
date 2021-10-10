package br.com.ovigia.businessrule.cliente.calcular;

import java.util.Date;
import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.calculadora.CalculadoraDistancia;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CalcularFrequenciaRondasRule
		implements BusinessRule<CalcularFrequenciaRondaRequest, List<CalcularFrequenciaRondaResponse>> {
	// Distancia em metros
	private final double distanciaMinima = 0.02d;
	// INTERVALO DE TOLERANCIA EM MILISEGUNDOS PARA TERMINAR A FREQUENCIA DA RONDA E
	// ELIMINAR O CENARIO EM QUE O VIGIA ESTA PARADO EM FRENTE A PROPRIEDADE DO
	// CLIENTE EM 5 MINUTOS
	private final long tolerancia = 5 * 60 * 1000;
	private ClienteRepository clienteRepository;
	private RondaRepository rotaRepository;

	private CalculadoraDistancia calculadoraDistancia = CalculadoraDistancia.calculadoraEsferica();

	public CalcularFrequenciaRondasRule(ClienteRepository clienteRepository, RondaRepository rotaRepository) {
		this.clienteRepository = clienteRepository;
		this.rotaRepository = rotaRepository;
	}

	@Override
	public Mono<Response<List<CalcularFrequenciaRondaResponse>>> apply(
			CalcularFrequenciaRondaRequest calculoFrequencia) {
		var data = DataUtil.gerarData(calculoFrequencia.getDataRonda());

		return clienteRepository.obterVigiasELocalizacao(calculoFrequencia.getIdCliente())
				.flatMap(cliente -> processarRotasPorId(cliente.vigias, data, cliente.localizacao))
				.map(frequencias -> Response.ok(frequencias));
	}

	private int calcularNumeroRodas(Localizacao localizacaoCliente, List<Localizacao> localizacoesVigia) {
		if (localizacoesVigia == null || localizacoesVigia.isEmpty()) {
			return 0;
		}

		// Ordenando a lista por horario de inclusao para poder determinar a data
		// de inicio de ronda em frente a propriedade do cliente
		localizacoesVigia.sort((loc1, loc2) -> loc1.timestamp.compareTo(loc2.timestamp));

		int totalRondas = 0;
		Date dataInicio = null;
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

	private boolean isDataForaIntervalo(Date dataAntes, Date dataDepois) {
		return Math.abs(dataAntes.getTime() - dataDepois.getTime()) >= tolerancia;
	}

	private Mono<List<CalcularFrequenciaRondaResponse>> processarRotasPorId(List<String> idsVigias, Date data,
			Localizacao localizacao) {
		return Flux.fromIterable(idsVigias).flatMap(idVigia -> {
			return calcularFrequenciaRonda(new IdRonda(idVigia, data), localizacao);
		}).collectList();

	}

	private Mono<CalcularFrequenciaRondaResponse> calcularFrequenciaRonda(IdRonda idRonda, Localizacao localizacao) {
		return rotaRepository.obterRondaPorId(idRonda).map(rota -> {
			var frequenciaRonda = new CalcularFrequenciaRondaResponse();
			var totalRondas = calcularNumeroRodas(localizacao, rota.localizacoes);
			frequenciaRonda.setIdVigia(rota.obterIdVigia());
			frequenciaRonda.setTotalRonda(totalRondas);
			return frequenciaRonda;
		});

	}
}
