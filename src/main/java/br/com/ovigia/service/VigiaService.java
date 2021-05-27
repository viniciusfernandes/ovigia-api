package br.com.ovigia.service;

import java.util.Arrays;

import br.com.ovigia.model.Vigia;
import reactor.core.publisher.Mono;

public class VigiaService {

	private final VigiaRepository repository;

	public VigiaService(VigiaRepository repository) {
		this.repository = repository;
	}

	public Mono<Integer> salvar(Vigia vigia) {
		return repository.salvar(vigia);
	}

	public Mono<Response> obter() {
		return Mono.just(new Response(ResponseStatus.BAD_REQUEST, Arrays.asList("CCCCCCCCC")));
	}

	public Mono<Response> obterById(Integer idVigia) {
		return Mono.just(new Response(ResponseStatus.OK, new Vigia(idVigia, "Vinicius")));
	}
}
