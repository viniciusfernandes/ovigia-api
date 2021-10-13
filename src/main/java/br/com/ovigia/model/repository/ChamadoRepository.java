package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.ChamadoParser.toDoc;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Chamado;
import reactor.core.publisher.Mono;

public class ChamadoRepository {
	private final MongoCollection<Document> collection;

	public ChamadoRepository(MongoDatabase database) {
		collection = database.getCollection("chamado");
	}

	public Mono<String> criar(Chamado chamado) {
		chamado.id = UUID.randomUUID().toString();
		var doc = toDoc(chamado);
		return Mono.from(collection.insertOne(doc)).thenReturn(chamado.id);
	}

}
