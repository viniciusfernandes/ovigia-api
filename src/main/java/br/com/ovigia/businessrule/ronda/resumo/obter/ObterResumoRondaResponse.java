package br.com.ovigia.businessrule.ronda.resumo.obter;

import br.com.ovigia.businessrule.util.DataUtil;

import java.util.Date;

public class ObterResumoRondaResponse {
	public Double distancia = 0d;
	public Long totalChamados = 0l;
	public Double tempo = 0d;
	public Character escalaTempo = 'h';
	public String data = "";
	public String hora = "";

	public ObterResumoRondaResponse inicializar() {
		distancia = 0d;
		totalChamados = 0l;
		tempo = 0d;
		escalaTempo = 'h';
		var datahora = DataUtil.obterDataHora(new Date());
		data = datahora.data;
		hora = datahora.hora;
		return this;
	}
}
