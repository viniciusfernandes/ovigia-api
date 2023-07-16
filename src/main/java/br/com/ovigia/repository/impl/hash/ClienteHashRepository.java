package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.repository.ClienteRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class ClienteHashRepository implements ClienteRepository {
    private final Map<String, Cliente> table = new HashMap<>();

    public Mono<Void> criarCliente(Cliente cliente) {
        table.put(cliente.id, cliente);
        return Mono.empty();
    }

    public Mono<FrequenciaRonda> obterFrequenciaRondaPorIdCliente(String idCliente) {
        var cliente = table.get(idCliente);
        if (cliente != null && cliente.frequenciaRonda != null) {
            return Mono.just(cliente.frequenciaRonda);
        }
        return Mono.empty();
    }

    public Mono<Long> atualizarIdVigia(String idVigia, String idCliente) {
        var cliente = table.get(idCliente);
        if (cliente == null) {
            return Mono.just(0L);
        }
        cliente.idVigia = idVigia;
        return Mono.just(1L);
    }

    public Mono<Cliente> obterIdVigiaELocalizacaoByIdCliente(String idCliente) {
        var cliente = table.get(idCliente);
        if (cliente == null) {
            return Mono.empty();
        }
        return Mono.just(cliente);
    }

    public Mono<Void> atualizarLocalizacaoPorId(String idCliente, Localizacao localizacao) {
        var cliente = table.get(idCliente);
        if (cliente == null) {
            return Mono.empty();
        }
        cliente.localizacao = localizacao;
        return Mono.empty();
    }

    public Mono<FrequenciaRonda> atualizarFrequenciaRonda(String idCliente, FrequenciaRonda frequencia) {
        var cliente = table.get(idCliente);
        if (cliente == null) {
            return Mono.empty();
        }
        cliente.frequenciaRonda = frequencia;
        return Mono.empty();
    }

}
