package br.com.ovigia.service;

import br.com.ovigia.model.Cliente;
import reactor.core.publisher.Mono;

public interface ClienteService {
	Mono<Response> salvar(Cliente cliente);
}
