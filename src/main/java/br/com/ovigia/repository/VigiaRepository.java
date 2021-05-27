package br.com.ovigia.repository;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Vigia;
import reactor.core.publisher.Mono;

public class VigiaRepository {
	private final MongoCollection<Document> collection;

	public VigiaRepository(MongoDatabase database) {
		collection = database.getCollection("vigia");
	}

	public Mono<String> salvar(Vigia vigia) {
		var id = UUID.randomUUID().toString();
		var docvigia = new Document("_id", id).append("nome", vigia.getNome());
		return Mono.from(collection.insertOne(docvigia)).map(doc -> id);
	}

	public Mono<Vigia> buscarPorId(String idVigia) {
		var mvigia = collection.find(new Document("_id", idVigia));
		return Mono.from(mvigia).map(doc -> {
			return new Vigia(doc.getString("_id"), doc.getString("nome"));
		});
	}

}
