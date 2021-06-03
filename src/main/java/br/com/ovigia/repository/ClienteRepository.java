package br.com.ovigia.repository;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Cliente;
import reactor.core.publisher.Mono;

public class ClienteRepository {
	private final MongoCollection<Document> collection;

	public ClienteRepository(MongoDatabase database) {
		collection = database.getCollection("cliente");
	}

	public Mono<String> criar(Cliente cliente) {
		var id = UUID.randomUUID().toString();
		var docvigia = new Document("_id", id).append("nome", cliente.getNome()).append("email", cliente.getEmail())
				.append("telefone", cliente.getTelefone());

		return Mono.from(collection.insertOne(docvigia)).map(doc -> id);
	}

	public Mono<Cliente> buscarPorId(String idCliente) {
		return Mono.from(collection.find(new Document("_id", idCliente))).map(doc -> {
			var cliente = new Cliente();
			cliente.setId(doc.getString("_id"));
			cliente.setNome(doc.getString("nome"));
			cliente.setEmail(doc.getString("email"));
			cliente.setTelefone(doc.getString("telefone"));
			return cliente;
		});
	}

}
