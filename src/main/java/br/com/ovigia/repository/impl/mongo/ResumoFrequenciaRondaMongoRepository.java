
package br.com.ovigia.repository.impl.mongo;

import br.com.ovigia.model.ResumoFrequenciaRonda;
import br.com.ovigia.model.repository.ResumoFrequenciaRondaRepository;
import br.com.ovigia.model.repository.ResumoRondaRepository;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import org.bson.Document;
import reactor.core.publisher.Mono;

import static br.com.ovigia.repository.parser.ResumoFrequenciaRondaParser.toDoc;

public class ResumoFrequenciaRondaMongoRepository implements ResumoFrequenciaRondaRepository {
	private final MongoCollection<Document> collection;

	public ResumoFrequenciaRondaMongoRepository(MongoDatabase database) {
		collection = database.getCollection("resumoFrequenciaRonda");
	}

	public Mono<ResumoFrequenciaRonda> criarResumo(ResumoFrequenciaRonda resumo) {
		var doc = toDoc(resumo);
		return Mono.from(collection.insertOne(doc)).thenReturn(resumo);
	}

}
