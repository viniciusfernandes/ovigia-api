package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.Avaliacao;
import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.enumeration.TipoUsuario;
import br.com.ovigia.model.repository.VigiaRepository;
import br.com.ovigia.repository.parser.AvaliacaoParser;
import br.com.ovigia.repository.parser.ClienteParser;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import br.com.ovigia.repository.parser.VigiaParser;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;

public class VigiaMongoRepository implements VigiaRepository {
    private final MongoCollection<Document> collection;

    public VigiaMongoRepository(MongoDatabase database) {
        collection = database.getCollection("usuario");
    }

    public Mono<Vigia> obterNomeETelefoneEAvaliacao(String idVigia) {
        var match = new Document("$match", new Document("_id", idVigia));
        var fields = new Document("nome", 1).append("telefone", 1).append("avaliacao", 1);
        var project = new Document("$project", fields);
        var list = Arrays.asList(match, project);
        return Mono.from(collection.aggregate(list)).map(docUsuario -> VigiaParser.fromDoc(docUsuario));
    }

    public Mono<Vigia> obterDataUltimaRonda(String idVigia) {
        var notEqualNull = new Document("$ne", null);
        var filter = new Document("_id", idVigia).append("dataUltimaRonda", notEqualNull).append("dataAtualizacaoRonda",
                notEqualNull);
        var match = new Document("$match", filter);
        var fields = new Document("dataUltimaRonda", 1).append("dataAtualizacaoRonda", 1);
        var project = new Document("$project", fields);
        var list = Arrays.asList(match, project);
        return Mono.from(collection.aggregate(list)).flatMap(doc -> {
            var vigia = new Vigia();
            vigia.dataUltimaRonda = doc.getDate("dataUltimaRonda");
            vigia.dataAtualizacaoRonda = doc.getDate("dataAtualizacaoRonda");
            return Mono.just(vigia);
        });
    }

    public Mono<Vigia> obterVigiaPorId(String idVigia) {
        return Mono.from(collection.find(new Document("_id", idVigia))).map(VigiaParser::fromDoc);
    }

    public Flux<Vigia> obterLocalizacaoVigias() {
        var match = new Document("$match", new Document("tipoUsuario", TipoUsuario.VIGIA.toString()));
        var fields = new Document("_id", 1).append("localizacao", 1);
        var project = new Document("$project", fields);
        var pipeline = Arrays.asList(match, project);
        return Flux.from(collection.aggregate(pipeline)).map(doc -> VigiaParser.fromDoc(doc));
    }

    public Mono<Long> atualizarDataUltimaRonda(String idVigia, Date dataUltimaRonda, Date dataAtualizacaoRonda) {
        var filter = new Document("_id", idVigia);
        var update = new Document("$set",
                new Document("dataUltimaRonda", dataUltimaRonda).append("dataAtualizacaoRonda", dataAtualizacaoRonda));
        return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
    }

    public Mono<Long> atualizarAvaliacao(String idVigia, Avaliacao avaliacao) {
        var filter = new Document("_id", idVigia);
        var update = new Document("$set", new Document("avaliacao", AvaliacaoParser.toDoc(avaliacao)));
        return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
    }

    public Mono<Avaliacao> obterAvaliacao(String idVigia) {
        var match = new Document("$match", new Document("_id", idVigia));
        var project = new Document("$project", new Document("avaliacao", 1));
        var pipeline = Arrays.asList(match, project);
        return Mono.from(collection.aggregate(pipeline)).flatMap(result -> {
            var docAvaliacao = result.get("avaliacao", Document.class);
            var avaliacao = AvaliacaoParser.fromDoc(docAvaliacao);
            if (avaliacao == null) {
                avaliacao = new Avaliacao();
            }
            return Mono.just(avaliacao);
        });
    }

    public Mono<Void> atualizarCliente(String idVigia, Cliente cliente) {
        var docId = new Document("_id", idVigia);
        var docCliente = ClienteParser.toDoc(cliente);

        var adicionarCliente = new Document("$push", new Document("clientes", docCliente));
        return Mono.from(collection.updateOne(docId, adicionarCliente)).then();
    }

    public Mono<Void> atualizarLocalizacaoPorId(String idVigia, Localizacao localizacao) {
        var docLocalizacao = new Document("localizacao", LocalizacaoParser.toDoc(localizacao));
        var update = new Document("$set", docLocalizacao);
        return Mono.from(collection.updateOne(new Document("_id", idVigia), update)).then();
    }
}
