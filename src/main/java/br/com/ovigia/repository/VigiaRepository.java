package br.com.ovigia.repository;

import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.repository.parser.ClienteParser;
import reactor.core.publisher.Mono;

public class VigiaRepository {
	private final MongoCollection<Document> collection;

	public VigiaRepository(MongoDatabase database) {
		collection = database.getCollection("vigia");
	}

	public Mono<String> criar(Vigia vigia) {
		var id = UUID.randomUUID().toString();
		var docvigia = new Document("_id", id).append("nome", vigia.getNome()).append("email", vigia.getEmail())
				.append("telefone", vigia.getTelefone());

		return Mono.from(collection.insertOne(docvigia)).map(doc -> id);
	}

	public Mono<Vigia> buscarPorId(String idVigia) {
		var mvigia = collection.find(new Document("_id", idVigia));
		return Mono.from(mvigia).map(doc -> {
			var vigia = new Vigia(doc.getString("_id"), doc.getString("nome"));
			vigia.setEmail(doc.getString("email"));
			vigia.setTelefone(doc.getString("telefone"));
			return vigia;
		});
	}

	public Mono<Void> atualizarCliente(String idVigia, Cliente cliente) {
		var docId = new Document("_id", idVigia);
		var docCliente = ClienteParser.toDoc(cliente);

		var adicionarCliente = new Document("$push", new Document("clientes", docCliente));
		return Mono.from(collection.updateOne(docId, adicionarCliente)).then();
	}

	public Mono<Void> atualizarLocalizacaoPorId(String idVigia, Localizacao localizacao) {
		var docLocalizacao = new Document("localizacao", ClienteParser.toDoc(localizacao));
		var update = new Document("$set", docLocalizacao);
		return Mono.from(collection.updateOne(new Document("_id", idVigia), update)).then();
	}
}
