
package br.com.ovigia.model.repository;

import java.util.Date;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Mensalidade;
import br.com.ovigia.repository.parser.MensalidadeParser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MensalidadeRepository {
	private final MongoCollection<Document> collection;

	public MensalidadeRepository(MongoDatabase database) {
		collection = database.getCollection("mensalidade");
	}

	public Mono<Mensalidade> criarMensalidade(Mensalidade mensalidade) {
		mensalidade.id = UUID.randomUUID().toString();
		var doc = MensalidadeParser.toDoc(mensalidade);
		return Mono.from(collection.insertOne(doc)).map(x -> mensalidade);
	}

	public Flux<Mensalidade> obterMensalidadesVencidasByIdContrato(String idContrato) {
		var filter = new Document("idContrato", idContrato).append("dataVencimento", new Document("$lte", new Date()))
				.append("dataPagamento", null);
		return Flux.from(collection.find(filter)).map(MensalidadeParser::fromDoc);
	}

}
