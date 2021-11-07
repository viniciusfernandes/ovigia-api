package br.com.ovigia.model.calculadora;

import br.com.ovigia.model.Localizacao;

  class CalculadoraDistanciaEuclidiana extends CalculadoraRonda{
	

	@Override
	public double distanciaOf(Localizacao loc1, Localizacao loc2) {
		var lat2 = loc1.latitude* loc1.latitude;
		var long2 = loc1.longitude* loc1.longitude;
		return Math.pow(lat2+long2, 0.5);
	}
}
