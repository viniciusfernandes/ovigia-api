
package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.ResumoRonda;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import br.com.ovigia.repository.parser.IdRondaParser;
import br.com.ovigia.repository.parser.ResumoRondaParser;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Mono;

public class ResumoRondaMongoRepository implements ResumoRondaRepository {
    private final MongoCollection<Document> collection;

    public ResumoRondaMongoRepository(MongoDatabase database) {
        collection = database.getCollection("resumoRonda");
    }

    public Mono<Void> criarResumoRonda(ResumoRonda resumoRonda) {
        var doc = ResumoRondaParser.toDoc(resumoRonda);
        return Mono.from(collection.insertOne(doc)).then();
    }

    public Mono<Void> atualizarResumoRonda(ResumoRonda resumoRonda) {
        var filter = IdRondaParser.toDoc(resumoRonda.id);
        var update = new Document("$set", ResumoRondaParser.toDocFields(resumoRonda));
        return Mono.from(collection.updateOne(filter, update)).then();
    }

    public Mono<ResumoRonda> obterResumoRondaById(IdRonda idRonda) {
        var id = IdRondaParser.toDoc(idRonda);
        return Mono.from(collection.find(id)).map(doc -> ResumoRondaParser.fromDoc(doc))
                .switchIfEmpty(Mono.just(new ResumoRonda()));
    }

}
