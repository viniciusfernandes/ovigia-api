package br.com.ovigia.repository;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.repository.parser.ClienteParser;
import reactor.core.publisher.Mono;

public class ClienteRepository {
	private final MongoCollection<Document> collection;

	public ClienteRepository(MongoDatabase database) {
		collection = database.getCollection("cliente");
	}

	public Mono<String> criar(Cliente cliente) {
		var id = UUID.randomUUID().toString();
		cliente.setId(id);

		var docCliente = ClienteParser.toDoc(cliente);
		return Mono.from(collection.insertOne(docCliente)).map(doc -> id);
	}

	public Mono<Cliente> buscarPorId(String idCliente) {
		return Mono.from(collection.find(new Document("_id", idCliente))).map(doc -> ClienteParser.fromDoc(doc));
	}

}
