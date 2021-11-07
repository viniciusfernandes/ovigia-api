
package br.com.ovigia.model.repository;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.ResumoRonda;
import br.com.ovigia.repository.parser.ResumoRondaParser;
import reactor.core.publisher.Mono;

public class ResumoRondaRepository {
	private final MongoCollection<Document> collection;

	public ResumoRondaRepository(MongoDatabase database) {
		collection = database.getCollection("resumoRonda");
	}

	public Mono<Void> criarResumoRonda(ResumoRonda resumoRonda) {
		var doc = ResumoRondaParser.toDoc(resumoRonda);
		return Mono.from(collection.insertOne(doc)).then();
	}

	public Mono<Void> atualizarResumoRonda(ResumoRonda resumoRonda) {
		var filter = new Document("_id", resumoRonda.idVigia);
		var update = new Document("$set", ResumoRondaParser.toDocFields(resumoRonda));
		return Mono.from(collection.updateOne(filter, update)).then();
	}

	public Mono<Void> removerResumoRonda(String idVigia) {
		var filter = new Document("_id", idVigia);
		Mono.from(collection.deleteOne(filter)).subscribe(r -> System.out.println("Removeu o resumo: " + r));
		return Mono.from(collection.deleteOne(filter)).then();
	}

	public Mono<ResumoRonda> obterResumoRondaByIdVigia(String idVigia) {
		var id = new Document("_id", idVigia);
		return Mono.from(collection.find(id)).map(doc -> ResumoRondaParser.fromDoc(doc))
				.switchIfEmpty(Mono.just(new ResumoRonda()));
	}

}
