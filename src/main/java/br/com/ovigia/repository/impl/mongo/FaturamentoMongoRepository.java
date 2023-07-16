
package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.Faturamento;
import br.com.ovigia.model.IdFaturamento;
import br.com.ovigia.model.repository.FaturamentoRepository;
import br.com.ovigia.repository.parser.FaturamentoParser;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static br.com.ovigia.repository.parser.FaturamentoParser.toDoc;

public class FaturamentoMongoRepository implements FaturamentoRepository {
	private final MongoCollection<Document> collection;

	public FaturamentoMongoRepository(MongoDatabase database) {
		collection = database.getCollection("faturamento");
	}

	public Mono<Faturamento> criarFaturamento(Faturamento faturamento) {
		var doc = toDoc(faturamento);
		return Mono.from(collection.insertOne(doc)).thenReturn(faturamento);
	}

	public Mono<Long> atualizarFaturamento(Faturamento faturamento) {
		var filter = toDoc(faturamento.id);
		var update = new Document("$set", new Document("quantidadePagamentos", faturamento.quantidadePagamentos)
				.append("valor", faturamento.valor));
		return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
	}

	public Mono<Faturamento> obterValoresFaturamento(IdFaturamento id) {
		var match = new Document("$match", toDoc(id));
		var project = new Document("$project", new Document("quantidadePagamentos", 1).append("valor", 1));
		var pipeline = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(pipeline)).map(FaturamentoParser::fromDoc);
	}

}
