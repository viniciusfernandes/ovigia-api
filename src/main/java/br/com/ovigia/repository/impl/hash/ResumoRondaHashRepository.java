
package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.ResumoRonda;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

public class ResumoRondaHashRepository implements ResumoRondaRepository {
    private final Map<IdRonda, ResumoRonda> table = new HashMap<>();

    public Mono<Void> criarResumoRonda(ResumoRonda resumoRonda) {
        table.put(resumoRonda.id, resumoRonda);
        return Mono.empty();
    }

    public Mono<Void> atualizarResumoRonda(ResumoRonda resumoRonda) {
        table.put(resumoRonda.id, resumoRonda);
        return Mono.empty();
    }

    public Mono<ResumoRonda> obterResumoRondaById(IdRonda idRonda) {
        if (!table.containsKey(idRonda)) {
            return Mono.empty();
        }
        return Mono.just(table.get(idRonda));
    }
}
