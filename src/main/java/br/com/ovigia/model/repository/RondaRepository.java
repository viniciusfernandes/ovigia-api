
package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.IdRondaParser.toDoc;

import java.util.Arrays;
import java.util.Date;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.ResumoRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import br.com.ovigia.repository.parser.ResumoRondaParser;
import br.com.ovigia.repository.parser.RondaParser;
import reactor.core.publisher.Mono;

public interface RondaRepository {

    Mono<Void> criarLocalizacao(String idVigia, Date data, Localizacao localizacao);

    Mono<Void> criarRonda(Ronda ronda);

    Mono<Void> criarResumoRonda(ResumoRonda resumoRonda);

    Mono<Ronda> obterRondaPorId(IdRonda id);

    Mono<Void> concatenarRonda(Ronda ronda);

    Mono<Ronda> obterUltimaRondaByIdVigia(String idvigia);

}
