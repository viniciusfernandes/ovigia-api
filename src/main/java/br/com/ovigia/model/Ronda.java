package br.com.ovigia.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.ovigia.model.calculadora.CalculadoraDistancia;

public class Ronda {
	public IdRonda id;
	public List<Localizacao> localizacoes = new ArrayList<>();

	public Ronda() {
	}

	public Ronda(IdRonda id) {
		this.id = id;
	}

	public void add(Localizacao localizacao) {
		localizacoes.add(localizacao);
	}

	public String obterIdVigia() {
		if (id == null) {
			return null;
		}
		return id.idVigia;
	}

	public Date obterData() {
		if (id == null) {
			return null;
		}
		return id.data;
	}

//	public static void main(String... d) {
//		var ronda = new Ronda();
//		ronda.add(new Localizacao(new Date(), -23.68178d, -46.62335));
//		ronda.add(new Localizacao(new Date(), -23.68133d, -46.62325));
//		ronda.add(new Localizacao(new Date(), -23.68186d, -46.62286));
//		ronda.add(new Localizacao(new Date(), -23.68007d, -46.62029));
//
//		var calcEuclid = CalculadoraDistancia.calculadoraEuclidiana();
//		var calcEsfer = CalculadoraDistancia.calculadoraEsferica();
//		System.out.println(calcEuclid.calcularDistancia(ronda));
//		System.out.println(calcEsfer.calcularDistancia(ronda));
//	}
}
