
package br.com.ovigia.model.repository;

import br.com.ovigia.model.ResumoFrequenciaRonda;
import reactor.core.publisher.Mono;

public interface ResumoFrequenciaRondaRepository {

    Mono<ResumoFrequenciaRonda> criarResumo(ResumoFrequenciaRonda resumo);
}
