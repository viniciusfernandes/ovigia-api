package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.RondaParser.*;
import static br.com.ovigia.repository.parser.RondaParser.toIdDoc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.IdRonda;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import reactor.core.publisher.Mono;

public class RondaRepository {
	private final MongoCollection<Document> collection;

	public RondaRepository(MongoDatabase database) {
		collection = database.getCollection("ronda");
	}

	public Mono<Void> criarLocalizacao(String idVigia, Date data, Localizacao localizacao) {

		var idRota = toIdDoc(idVigia, data);
		var doclocalizacao = LocalizacaoParser.toDoc(localizacao);
		var adicionarLocalizacao = new Document("$push", new Document("localizacoes", doclocalizacao));

		return Mono.from(collection.updateMany(idRota, adicionarLocalizacao)).then();
	}

	public Mono<Void> criar(Ronda ronda) {
		var docRota = toDoc(ronda);
		return Mono.from(collection.insertOne(docRota)).then();
	}

	public Mono<Ronda> obterRondaPorId(IdRonda id) {
		return Mono.from(collection.find(toIdDoc(id))).map(docRonda -> {
			var docId = docRonda.get("_id", Document.class);

			var idRonda = new IdRonda();
			idRonda.idVigia = docId.getString("idVigia");
			idRonda.data = docId.getDate("data");

			var ronda = new Ronda(idRonda);

			@SuppressWarnings("unchecked")
			var localizacoes = (List<Document>) docRonda.get("localizacoes");
			for (Document docLoc : localizacoes) {
				ronda.add(LocalizacaoParser.fromNestedDoc(docLoc));
			}
			return ronda;
		});
	}

	public Mono<Boolean> contemRonda(IdRonda id) {
		var match = new Document("$match", toIdDoc(id));
		var fields = new Document("_id", 1);
		var project = new Document("$project", fields);

		var list = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(list)).map(ronda -> true).switchIfEmpty(Mono.just(false));
	}

	public Mono<Void> atualizarLocalizacoes(Ronda ronda) {
		var each = new Document("$each", LocalizacaoParser.toDoc(ronda.localizacoes));
		var docLocalizacoes = new Document("localizacoes", each);
		var update = new Document("$push", docLocalizacoes).append("$set", new Document("fim", ronda.fim	));
		return Mono.from(collection.updateOne(toIdDoc(ronda.id), update)).then();
	}
}
