package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.repository.ClienteRepository;
import br.com.ovigia.repository.parser.ClienteParser;
import br.com.ovigia.repository.parser.FrequenciaRondaParser;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public class ClienteMongoRepository implements ClienteRepository {
    private final MongoCollection<Document> collection;

    public ClienteMongoRepository(MongoDatabase database) {
        collection = database.getCollection("usuario");
    }

    public Mono<Void> criarCliente(Cliente cliente) {
        var doc = ClienteParser.toDoc(cliente);
        return Mono.from(collection.insertOne(doc)).then();
    }

    public Mono<Cliente> obterClientePorId(String idCliente) {
        return Mono.from(collection.find(new Document("_id", idCliente))).map(doc -> ClienteParser.fromDoc(doc));
    }

    public Mono<FrequenciaRonda> obterFrequenciaRondaPorIdCliente(String idCliente) {
        var match = new Document("$match",
                new Document("_id", idCliente).append("frequenciaRonda", new Document("$ne", null)));
        var project = new Document("$project", new Document("frequenciaRonda", 1));
        var pipeline = Arrays.asList(match, project);
        return Mono.from(collection.aggregate(pipeline)).map(doc -> {
            var frequencia = doc.get("frequenciaRonda", Document.class);
            return FrequenciaRondaParser.fromDoc(frequencia);
        }).switchIfEmpty(Mono.just(new FrequenciaRonda()));
    }

    public Mono<Long> atualizarIdVigia(String idVigia, String idCliente) {
        var filter = new Document("_id", idCliente);
        var update = new Document("$set", new Document("idVigia", idVigia));
        return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
    }

    public Mono<Cliente> obterIdVigiaELocalizacaoByIdCliente(String idCliente) {
        var match = new Document("$match", new Document("_id", idCliente));
        var project = new Document("$project", new Document("idVigia", 1).append("localizacao", 1));
        var pipeline = Arrays.asList(match, project);
        return Mono.from(collection.aggregate(pipeline)).map(ClienteParser::fromDoc);
    }

    public Mono<Void> atualizarLocalizacaoPorId(String idCliente, Localizacao localizacao) {
        var docLocalizacao = new Document("localizacao", LocalizacaoParser.toDoc(localizacao));
        var update = new Document("$set", docLocalizacao);
        return Mono.from(collection.updateOne(new Document("_id", idCliente), update)).then();
    }

    public Mono<FrequenciaRonda> atualizarFrequenciaRonda(String idCliente, FrequenciaRonda frequencia) {
        var doc = FrequenciaRondaParser.toDoc(frequencia);
        var update = new Document("$set", new Document("frequenciaRonda", doc));
        return Mono.from(collection.updateOne(new Document("_id", idCliente), update)).thenReturn(frequencia);
    }

}
