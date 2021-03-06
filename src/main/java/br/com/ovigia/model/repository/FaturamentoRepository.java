
package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.FaturamentoParser.toDoc;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Faturamento;
import br.com.ovigia.model.IdFaturamento;
import br.com.ovigia.repository.parser.FaturamentoParser;
import reactor.core.publisher.Mono;

public class FaturamentoRepository {
	private final MongoCollection<Document> collection;

	public FaturamentoRepository(MongoDatabase database) {
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
