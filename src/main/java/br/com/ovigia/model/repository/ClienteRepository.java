package br.com.ovigia.model.repository;

import java.util.Arrays;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.FrequenciaRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.repository.parser.ClienteParser;
import br.com.ovigia.repository.parser.FrequenciaRondaParser;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import reactor.core.publisher.Mono;

public class ClienteRepository {
	private final MongoCollection<Document> collection;

	public ClienteRepository(MongoDatabase database) {
		collection = database.getCollection("usuario");
	}

	public Mono<Void> criarCliente(Cliente cliente) {
		var doc = ClienteParser.toDoc(cliente);
		return Mono.from(collection.insertOne(doc)).then();
	}

	public Mono<Cliente> obterClientePorId(String idCliente) {
		return Mono.from(collection.find(new Document("_id", idCliente))).map(doc -> ClienteParser.fromDoc(doc));
	}

	public Mono<Void> atualizarVigia(String idVigia, String idCliente) {
		var filter = new Document("_id", idCliente);
		var update = new Document("$set", new Document("idVigia", idVigia));
		return Mono.from(collection.updateOne(filter, update)).then();
	}

	public Mono<Localizacao> obterLocalizacaoCliente(String idCliente) {
		var match = new Document("$match", new Document("_id", idCliente));
		var project = new Document("$project", new Document("localizacao", 1));
		var pipeline = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(pipeline)).map(doc -> LocalizacaoParser.fromDoc(doc));
	}

	public Mono<Void> atualizarLocalizacaoPorId(String idCliente, Localizacao localizacao) {
		var docLocalizacao = new Document("localizacao", LocalizacaoParser.toDoc(localizacao));
		var update = new Document("$set", docLocalizacao);
		return Mono.from(collection.updateOne(new Document("_id", idCliente), update)).then();
	}

	public Mono<Void> atualizarFrequenciaRonda(FrequenciaRonda frequencia) {
		var doc = FrequenciaRondaParser.toDocFlat(frequencia);
		var update = new Document("$set", new Document("frequenciaRonda", doc));
		return Mono.from(collection.updateOne(new Document("_id", frequencia.id.idCliente), update)).then();
	}

}
