
package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.repository.RondaRepository;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RondaHashRepository implements RondaRepository {
    private final Map<IdRonda, Ronda> table = new HashMap<>();

    public Mono<Void> criarLocalizacao(String idVigia, Date data, Localizacao localizacao) {
        var ronda = table.get(new IdRonda(idVigia, data));
        if (ronda != null) {
            ronda.add(localizacao);
        }
        return Mono.empty();
    }

    public Mono<Void> criarRonda(Ronda ronda) {
        table.put(ronda.id, ronda);
        return Mono.empty();
    }

    public Mono<Ronda> obterRondaPorId(IdRonda id) {
        var ronda = table.get(id);
        if (ronda != null) {
            return Mono.just(ronda);
        }
        return Mono.empty();
    }

    public Mono<Void> concatenarRonda(Ronda ronda) {
        var record = table.get(ronda.id);
        if (record != null) {
            record.dataAtualizacao = ronda.dataAtualizacao;
            record.fim = ronda.fim;
            record.add(ronda.localizacoes);
        }
        return Mono.empty();
    }
}
