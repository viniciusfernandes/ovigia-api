package br.com.ovigia.repository;

import java.util.Arrays;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.parser.ClienteParser;
import br.com.ovigia.repository.parser.LocalizacaoParser;
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

	public Mono<Cliente> obterPorId(String idCliente) {
		return Mono.from(collection.find(new Document("_id", idCliente))).map(doc -> ClienteParser.fromDoc(doc));
	}

	public Mono<Void> atualizarVigia(String idVigia, String idCliente) {
		var docIdCliente = new Document("_id", idCliente);

		var adicionarVigia = new Document("$push", new Document("vigias", idVigia));
		return Mono.from(collection.updateOne(docIdCliente, adicionarVigia)).then();
	}

	public Mono<Cliente> obterVigiasELocalizacao(String idCliente) {
		var match = new Document("$match", new Document("_id", idCliente));
		var fields = new Document("vigias", 1).append("localizacao", 1);
		var project = new Document("$project", fields);

		var pipeline = Arrays.asList(match, project);

		return Mono.from(collection.find(new Document("_id", idCliente))).map(doc -> {
			return ClienteParser.fromDoc(doc);
		});
		/*
		 * return Mono.from(collection.aggregate(pipeline)).map(document -> { return
		 * document.get("vigias", Cliente.class); });
		 */
	}

	public Mono<Void> atualizarLocalizacaoPorId(String idCliente, Localizacao localizacao) {
		var docLocalizacao = new Document("localizacao", LocalizacaoParser.toDoc(localizacao));
		var update = new Document("$set", docLocalizacao);
		return Mono.from(collection.updateOne(new Document("_id", idCliente), update)).then();
	}

}
