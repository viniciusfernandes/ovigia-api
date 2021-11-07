package br.com.ovigia.model.calculadora;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;

public abstract class CalculadoraRonda {
	public final static double MILISEGUNDOS_TO_HORAS_RATE = 1000.0 * 3600.d;

	public double calcularDistancia(Ronda ronda) {
		double totDist = 0;
		var localizacoes = ronda.localizacoes;
		final int size = localizacoes.size();
		final int last = size - 2;
		Localizacao l1 = null;
		Localizacao l2 = null;
		for (int i = 0; i < size; i++) {
			l1 = localizacoes.get(i);
			l2 = localizacoes.get(i + 1);
			totDist += distanciaOf(l1, l2);
			if (i >= last) {
				break;
			}
		}

		return round(totDist);
	}

	public TempoEscala calcularTempo(Ronda ronda) {
		var intervalo = (ronda.fim.getTime() - ronda.inicio.getTime()) / MILISEGUNDOS_TO_HORAS_RATE;

		var escala = EscalaTemporal.HORAS;
		if (intervalo < 0.09) {
			intervalo *= 60.0;
			escala = EscalaTemporal.MINUTOS;

			if (intervalo < 0.9) {
				intervalo *= 60.0;
				escala = EscalaTemporal.SEGUNDOS;
			}
		}

		intervalo = round(intervalo);
		return new TempoEscala(intervalo, escala.getPrefixo());
	}

	public double calcularTempo(Localizacao loc1, Localizacao loc2) {
		return (loc1.timestamp - loc2.timestamp) / MILISEGUNDOS_TO_HORAS_RATE;
	}

	public abstract double distanciaOf(Localizacao loc1, Localizacao loc2);

	public boolean isDistanciaOk(Localizacao loc1, Localizacao loc2, double distanciaMaxima) {
		return distanciaOf(loc1, loc2) <= distanciaMaxima;
	}

	public static CalculadoraRonda calculadoraEuclidiana() {
		return new CalculadoraDistanciaEuclidiana();
	}

	public static CalculadoraRonda calculadoraEsferica() {
		return new CalculadoraDistanciaEsferica();
	}

	public double round(double value) {
		return new BigDecimal(value).setScale(1, RoundingMode.HALF_UP).doubleValue();
	}
}

enum EscalaTemporal {
	SEGUNDOS('s'), MINUTOS('m'), HORAS('h');

	private char prefixo;

	private EscalaTemporal(char prefixo) {
		this.prefixo = prefixo;
	}

	public char getPrefixo() {
		return prefixo;
	}
}
