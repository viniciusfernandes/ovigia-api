
package br.com.ovigia.model.repository;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface RondaRepository {

    Mono<Void> criarLocalizacao(String idVigia, Date data, Localizacao localizacao);

    Mono<Void> criarRonda(Ronda ronda);


    Mono<Ronda> obterRondaPorId(IdRonda id);

    Mono<Void> concatenarRonda(Ronda ronda);

}
