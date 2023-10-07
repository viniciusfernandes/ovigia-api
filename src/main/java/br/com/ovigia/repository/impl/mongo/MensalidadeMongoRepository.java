
package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.Mensalidade;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.MensalidadeRepository;
import br.com.ovigia.repository.parser.MensalidadeParser;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class MensalidadeMongoRepository implements MensalidadeRepository {
    private final MongoCollection<Document> collection;

    public MensalidadeMongoRepository(MongoDatabase database) {
        collection = database.getCollection("mensalidade");
    }

    public Mono<Mensalidade> criarMensalidade(Mensalidade mensalidade) {
        mensalidade.id = UUID.randomUUID().toString();
        var doc = MensalidadeParser.toDoc(mensalidade);
        return Mono.from(collection.insertOne(doc)).map(x -> mensalidade);
    }

    public Mono<Long> atualizaDataPagamentoMensalidade(String idMensalidade, Date dataPagamento,
                                                       TipoSituacaoMensalidade situacao) {
        var filter = new Document("_id", idMensalidade);
        var fields = new Document("dataPagamento", dataPagamento).append("situacao", situacao.toString());
        var update = new Document("$set", fields);
        return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
    }

    public Flux<Mensalidade> obterMensalidadesDataVencimentoInferiorByIdVigia(String idVigia, Date dataLimite,
                                                                              TipoSituacaoMensalidade situacao) {
        var filter = new Document("idVigia", idVigia).append("dataVencimento", new Document("$lte", dataLimite))
                .append("dataPagamento", null).append("situacao", situacao.toString());

        var match = new Document("$match", filter);
        var project = new Document("$project", new Document("_id", 1).append("dataVencimento", 1)
                .append("dataVencimento", 1).append("valor", 1).append("telefoneCliente", 1).append("nomeCliente", 1));
        var sort = new Document("$sort", new Document("dataVencimento", 1));
        var pipeline = Arrays.asList(match, project, sort);
        return Flux.from(collection.aggregate(pipeline)).map(MensalidadeParser::fromDoc);
    }

    @Override
    public Mono<Mensalidade> obterMensalidadesbyIdContratoESituacaoInferiorADataMax(String idContrato, TipoSituacaoMensalidade situacao,
                                                                             Date dataMaxima) {
        var filter = new Document("idContrato", idContrato).append("situacao", situacao.toString())
                .append("dataVencimento", new Document("$lte", dataMaxima));

        var match = new Document("$match", filter);
        var sort  = new Document("$sort",  new Document("dataVencimento", 1));
        var limit   = new Document("$limit",  1);
        var pipeline = Arrays.asList(match, sort, limit);
        return Mono.from(collection.aggregate(pipeline)).map(MensalidadeParser::fromDoc);
    }
}
