package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.SolicitacaoVisita;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static br.com.ovigia.repository.parser.SolicitacaoVistiaParser.fromDoc;
import static br.com.ovigia.repository.parser.SolicitacaoVistiaParser.toDoc;

public class SolicitacaoVisitaHashRepository implements SolicitacaoVisitaRepository {
    private final MongoCollection<Document> collection;

    public SolicitacaoVisitaHashRepository(MongoDatabase database) {
        collection = database.getCollection("solicitacaoVisita");
    }

    public Mono<SolicitacaoVisita> criarSolicitacao(SolicitacaoVisita solicitacao) {
        var doc = toDoc(solicitacao);
        return Mono.from(collection.insertOne(doc)).map(Result -> solicitacao);
    }

    public Flux<SolicitacaoVisita> obterSolicitacaoByIdVigia(String idVigia) {
        return Flux.from(collection.find(new Document("idVigia", idVigia))).map(doc -> fromDoc(doc));
    }

    public Mono<String> obterIdVigiaSolicitado(String idCliente) {
        var match = new Document("$match", new Document("_id", idCliente));
        var project = new Document("$project", new Document("idVigia", 1));
        var pipeline = Arrays.asList(match, project);
        return Mono.from(collection.aggregate(pipeline)).map(doc -> doc.getString("idVigia"));
    }

    public Mono<Long> removerSolicitacao(String idCliente) {
        return Mono.from(collection.deleteOne(new Document("_id", idCliente))).map(result -> result.getDeletedCount());
    }

}
