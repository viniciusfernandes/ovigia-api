package br.com.ovigia.businessrule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import br.com.ovigia.model.IdRota;
import br.com.ovigia.model.Rota;
import br.com.ovigia.repository.RotaRepository;
import reactor.core.publisher.Mono;

public class CriarRotaRule implements BusinessRule<String, Void> {

	private RotaRepository repository;

	public CriarRotaRule(RotaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(String idVigia) {
		var rota = new Rota();
		IdRota id = new IdRota();
		id.setData(gerarData(new Date()));
		id.setIdVigia(idVigia);
		rota.setId(id);

		rota.setLocalizacoes(new ArrayList<>());

		return repository.criar(rota).thenReturn(new Response<>());
	}

	private Date gerarData(Date data) {
		var cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

}
