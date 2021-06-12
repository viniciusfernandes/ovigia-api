package br.com.ovigia.repository;

import java.util.ArrayList;
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

		var idRota = gerarIdRonda(idVigia, data);
		var doclocalizacao = LocalizacaoParser.toDoc(localizacao);
		var adicionarLocalizacao = new Document("$push", new Document("localizacoes", doclocalizacao));

		return Mono.from(collection.updateMany(idRota, adicionarLocalizacao)).then();
	}

	public Mono<Void> criar(Ronda ronda) {
		var docRota = gerarIdRonda(ronda.obterIdVigia(), ronda.obterData());
		docRota.append("localizacoes", new ArrayList<>());

		return Mono.from(collection.insertOne(docRota)).then();
	}

	public Mono<Ronda> obterRondaPorId(IdRonda id) {
		return obterRondaPorId(id.getIdVigia(), id.getData());
	}

	public Mono<Ronda> obterRondaPorId(String idVigia, Date data) {
		return Mono.from(collection.find(gerarIdRonda(idVigia, data))).map(docRonda -> {
			var docId = docRonda.get("_id", Document.class);

			var idRonda = new IdRonda();
			idRonda.setIdVigia(docId.getString("idVigia"));
			idRonda.setData(docId.getDate("data"));

			var ronda = new Ronda(idRonda);

			@SuppressWarnings("unchecked")
			var localizacoes = (List<Document>) docRonda.get("localizacoes");
			for (Document docLoc : localizacoes) {
				ronda.add(LocalizacaoParser.fromNestedDoc(docLoc));
			}
			return ronda;
		});
	}

	private Document gerarIdRonda(String idVigia, Date data) {
		var value = new Document().append("idVigia", idVigia).append("data", data);
		return new Document("_id", value);
	}
}