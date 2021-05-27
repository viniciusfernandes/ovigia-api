package br.com.ovigia.service;

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

	public Mono<Integer> salvar(Vigia vigia) {
		var docvigia = new Document().append("nome", vigia.getNome());
		collection.insertOne(docvigia);
		return Mono.just(222);
	}

}
