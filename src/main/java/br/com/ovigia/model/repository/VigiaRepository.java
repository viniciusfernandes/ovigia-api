package br.com.ovigia.model.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Cliente;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Vigia;
import br.com.ovigia.model.enumeration.TipoUsuario;
import br.com.ovigia.repository.parser.ClienteParser;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import br.com.ovigia.repository.parser.VigiaParser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class VigiaRepository {
	private final MongoCollection<Document> collection;

	public VigiaRepository(MongoDatabase database) {
		collection = database.getCollection("usuario");
	}

	public Mono<Vigia> obterNomeETelefoneEAvaliacao(String idVigia) {
		var match = new Document("$match", new Document("_id", idVigia));
		var fields = new Document("nome", 1).append("telefone", 1).append("avaliacao", 1);
		var project = new Document("$project", fields);
		var list = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(list)).map(docUsuario -> VigiaParser.fromDoc(docUsuario));
	}

	public Mono<Date> obterDataUltimaRonda(String idVigia) {
		var match = new Document("$match", new Document("_id", idVigia));
		var fields = new Document("dataUltimaRonda", 1);
		var project = new Document("$project", fields);
		var list = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(list)).map(doc -> doc.getDate("dataUltimaRonda"));
	}

	public Mono<Vigia> obterVigiaPorId(String idVigia) {
		return Mono.from(collection.find(new Document("_id", idVigia))).map(VigiaParser::fromDoc);
	}

	public Flux<Vigia> obterLocalizacaoVigias() {
		var match = new Document("$match", new Document("tipoUsuario", TipoUsuario.VIGIA.toString()));
		var fields = new Document("_id", 1).append("localizacao", 1);
		var project = new Document("$project", fields);
		var pipeline = Arrays.asList(match, project);
		return Flux.from(collection.aggregate(pipeline)).map(doc -> VigiaParser.fromDoc(doc));
	}

	public Flux<Vigia> obterVigias(List<String> idVigias) {
		var in = new Document("_id", new Document("$in", idVigias));
		var match = new Document("$match", in);
		var fields = new Document("_id", 1).append("localizacao", 1).append("nome", 1).append("dataInicio", 1)
				.append("telefone", 1);
		var project = new Document("$project", fields);
		var pipeline = Arrays.asList(match, project);
		return Flux.from(collection.aggregate(pipeline)).map(doc -> VigiaParser.fromDoc(doc));
	}

	public Mono<Long> atualizarDataUltimaRonda(String idVigia, Date dataUltimaRonda) {
		var filter = new Document("_id", idVigia);
		var update = new Document("$set", new Document("dataUltimaRonda", dataUltimaRonda));
		return Mono.from(collection.updateOne(filter, update)).map(result -> result.getModifiedCount());
	}

	public Mono<Void> atualizarCliente(String idVigia, Cliente cliente) {
		var docId = new Document("_id", idVigia);
		var docCliente = ClienteParser.toDoc(cliente);

		var adicionarCliente = new Document("$push", new Document("clientes", docCliente));
		return Mono.from(collection.updateOne(docId, adicionarCliente)).then();
	}

	public Mono<Void> atualizarLocalizacaoPorId(String idVigia, Localizacao localizacao) {
		var docLocalizacao = new Document("localizacao", LocalizacaoParser.toDoc(localizacao));
		var update = new Document("$set", docLocalizacao);
		return Mono.from(collection.updateOne(new Document("_id", idVigia), update)).then();
	}
}
