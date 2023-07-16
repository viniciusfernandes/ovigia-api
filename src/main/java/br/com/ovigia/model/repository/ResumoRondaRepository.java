
package br.com.ovigia.model.repository;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.ResumoRonda;
import reactor.core.publisher.Mono;

public interface ResumoRondaRepository {

    Mono<Void> criarResumoRonda(ResumoRonda resumoRonda);

    Mono<Void> atualizarResumoRonda(ResumoRonda resumoRonda);

    Mono<ResumoRonda> obterResumoRondaById(IdRonda idRonda);
}
