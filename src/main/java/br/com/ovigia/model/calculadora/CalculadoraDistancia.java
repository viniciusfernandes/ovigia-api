package br.com.ovigia.model.calculadora;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;

public abstract class CalculadoraDistancia {
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

		return totDist;
	}

	public abstract double distanciaOf(Localizacao loc1, Localizacao loc2);

	public boolean isDistanciaOk(Localizacao loc1, Localizacao loc2, double distanciaMaxima) {
		return distanciaOf(loc1, loc2) <= distanciaMaxima;
	}

	public static CalculadoraDistancia calculadoraEuclidiana() {
		return new CalculadoraDistanciaEuclidiana();
	}

	public static CalculadoraDistancia calculadoraEsferica() {
		return new CalculadoraDistanciaEsferica();
	}
}
