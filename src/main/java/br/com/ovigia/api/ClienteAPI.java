package br.com.ovigia.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
public class ClienteAPI {

	@GetMapping("/{idCliente}")
	public Mono<Integer> obter(@PathVariable(name = "idCliente") Integer idCliente) {
		return Mono.just(555);
	}
}
