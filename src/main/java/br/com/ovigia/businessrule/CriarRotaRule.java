package br.com.ovigia.businessrule;

import java.util.Calendar;
import java.util.Date;

import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.RotaRepository;
import reactor.core.publisher.Mono;

public class CriarRotaRule implements BusinessRule<Localizacao, Void> {

	private RotaRepository repository;

	public CriarRotaRule(RotaRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Response<Void>> apply(Localizacao rota) {
		var dataAtual = new Date();
		rota.setHora(dataAtual);
		return repository.criar(rota.getIdVigia(), gerarData(dataAtual), rota).map(id -> new Response<>(id));
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
