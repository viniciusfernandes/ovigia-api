package br.com.ovigia.service;

import br.com.ovigia.model.Cliente;
import reactor.core.publisher.Mono;

public class ClienteService {
	public Mono<Response> salvar(Cliente cliente) {
		return Mono.empty();
	}
}
