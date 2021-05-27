package br.com.ovigia.service;

import java.util.Arrays;

import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.VigiaRepository;
import reactor.core.publisher.Mono;

public class VigiaService {

	private final VigiaRepository repository;

	public VigiaService(VigiaRepository repository) {
		this.repository = repository;
	}

	public Mono<String> salvar(Vigia vigia) {
		return repository.salvar(vigia);
	}

	public Mono<Response> buscar() {
		return Mono.just(new Response(ResponseStatus.BAD_REQUEST, Arrays.asList("CCCCCCCCC")));
	}

	public Mono<Response> buscarPorId(String idVigia) {
		return repository.buscarPorId(idVigia).map(vigia -> new Response(vigia));
	}
}
