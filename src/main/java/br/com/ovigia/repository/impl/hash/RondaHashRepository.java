
package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.ResumoRonda;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.model.repository.RondaRepository;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import br.com.ovigia.repository.parser.ResumoRondaParser;
import br.com.ovigia.repository.parser.RondaParser;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;

import static br.com.ovigia.repository.parser.IdRondaParser.toDoc;

public class RondaHashRepository implements RondaRepository {
    private final MongoCollection<Document> collection;

    public RondaHashRepository(MongoDatabase database) {
        collection = database.getCollection("ronda");
    }

    public Mono<Void> criarLocalizacao(String idVigia, Date data, Localizacao localizacao) {

        var idRota = toDoc(idVigia, data);
        var doclocalizacao = LocalizacaoParser.toDoc(localizacao);
        var adicionarLocalizacao = new Document("$push", new Document("localizacoes", doclocalizacao));

        return Mono.from(collection.updateMany(idRota, adicionarLocalizacao)).then();
    }

    public Mono<Void> criarRonda(Ronda ronda) {
        var docRota = RondaParser.toDoc(ronda);
        return Mono.from(collection.insertOne(docRota)).then();
    }

    public Mono<Void> criarResumoRonda(ResumoRonda resumoRonda) {
        var doc = ResumoRondaParser.toDoc(resumoRonda);
        return Mono.from(collection.insertOne(doc)).then();
    }

    public Mono<Ronda> obterRondaPorId(IdRonda id) {
        return Mono.from(collection.find(toDoc(id))).map(RondaParser::fromDoc);
    }

    public Mono<Void> concatenarRonda(Ronda ronda) {
        var each = new Document("$each", LocalizacaoParser.toDoc(ronda.localizacoes));
        var docLocalizacoes = new Document("localizacoes", each);
        var fields = new Document("fim", ronda.fim).append("dataAtualizacao", ronda.dataAtualizacao);
        var update = new Document("$push", docLocalizacoes).append("$set", fields);
        return Mono.from(collection.updateOne(toDoc(ronda.id), update)).then();
    }

    public Mono<Ronda> obterUltimaRondaByIdVigia(String idvigia) {
        var match = new Document("$match", new Document("_id.idVigia", idvigia));
        var fields = new Document("_id.data", 1).append("inicio", 1).append("fim", 1).append("localizacoes", 1);
        var project = new Document("$project", fields);
        var pipeline = Arrays.asList(match, project);
        return Mono.from(collection.aggregate(pipeline)).map(RondaParser::fromDoc);
    }

}
