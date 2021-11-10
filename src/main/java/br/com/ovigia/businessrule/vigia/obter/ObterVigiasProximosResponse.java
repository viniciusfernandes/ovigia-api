package br.com.ovigia.businessrule.vigia.obter;

import java.util.List;

public class ObterVigiasProximosResponse {

	public List<VigiaProximoResponse> vigias;

	public ObterVigiasProximosResponse(List<VigiaProximoResponse> vigias) {
		this.vigias = vigias;
	}
}
