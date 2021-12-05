
package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.ResumoFrequenciaRondaParser.toDoc;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.ResumoFrequenciaRonda;
import reactor.core.publisher.Mono;

public class ResumoFrequenciaRondaRepository {
	private final MongoCollection<Document> collection;

	public ResumoFrequenciaRondaRepository(MongoDatabase database) {
		collection = database.getCollection("resumoFrequenciaRonda");
	}

	public Mono<ResumoFrequenciaRonda> criarResumo(ResumoFrequenciaRonda resumo) {
		var doc = toDoc(resumo);
		return Mono.from(collection.insertOne(doc)).thenReturn(resumo);
	}

}
