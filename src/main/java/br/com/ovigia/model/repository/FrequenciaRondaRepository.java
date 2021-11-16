
package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.FrequenciaRondaParser.toDoc;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.FrequenciaRonda;
import reactor.core.publisher.Mono;

public class FrequenciaRondaRepository {
	private final MongoCollection<Document> collection;

	public FrequenciaRondaRepository(MongoDatabase database) {
		collection = database.getCollection("frequenciaRonda");
	}

	public Mono<Void> criarFrequenciaRonda(FrequenciaRonda frequenciaRonda) {
		var doc = toDoc(frequenciaRonda);
		return Mono.from(collection.insertOne(doc)).then();
	}

}
