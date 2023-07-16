package br.com.ovigia.model.repository;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.Localizacao;
import reactor.core.publisher.Mono;

public interface ClienteRepository {

    Mono<Void> criarCliente(Cliente cliente);

    Mono<FrequenciaRonda> obterFrequenciaRondaPorIdCliente(String idCliente);

    Mono<Long> atualizarIdVigia(String idVigia, String idCliente);

    Mono<Cliente> obterIdVigiaELocalizacaoByIdCliente(String idCliente);

    Mono<Void> atualizarLocalizacaoPorId(String idCliente, Localizacao localizacao);

    Mono<FrequenciaRonda> atualizarFrequenciaRonda(String idCliente, FrequenciaRonda frequencia);

}
