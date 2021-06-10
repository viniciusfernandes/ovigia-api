package br.com.ovigia.businessrule.rota;

import static br.com.ovigia.businessrule.util.DataUtil.gerarData;

import java.util.ArrayList;
import java.util.Date;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.IdRota;
import br.com.ovigia.model.Rota;
import br.com.ovigia.repository.RotaRepository;
import reactor.core.publisher.Mono;

public class CriarRotaRule implements BusinessRule<CriarRotaRequest, Void> {

	private RotaRepository repository;

	public CriarRotaRule(RotaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(CriarRotaRequest request) {
		var rota = new Rota();
		IdRota id = new IdRota();
		id.setData(gerarData(new Date()));
		id.setIdVigia(request.getIdVigia());
		rota.setId(id);

		rota.setLocalizacoes(new ArrayList<>());

		return repository.criar(rota).thenReturn(Response.nonResult());
	}

}
