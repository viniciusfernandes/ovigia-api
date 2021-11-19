
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

	public Mono<FrequenciaRonda> criarFrequenciaRonda(FrequenciaRonda frequencia) {
		var doc = toDoc(frequencia);
		return Mono.from(collection.insertOne(doc)).thenReturn(frequencia);
	}

}
