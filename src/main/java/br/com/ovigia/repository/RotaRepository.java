package br.com.ovigia.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.IdRota;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Rota;
import reactor.core.publisher.Mono;

public class RotaRepository {
	private final MongoCollection<Document> collection;

	public RotaRepository(MongoDatabase database) {
		collection = database.getCollection("rota");
	}

	public Mono<Void> criarLocalizacao(String idVigia, Date data, Localizacao localizacao) {

		var idRota = gerarIdRota(idVigia, data);
		var doclocalizacao = new Document();
		doclocalizacao.append("hora", localizacao.getHora());
		doclocalizacao.append("latitude", localizacao.getLatitude());
		doclocalizacao.append("longitude", localizacao.getLongitude());

		var adicionarLocalizacao = new Document("$push", new Document("localizacoes", doclocalizacao));

		return Mono.from(collection.updateMany(idRota, adicionarLocalizacao)).then();
	}

	public Mono<Void> criar(Rota rota) {
		var docRota = gerarIdRota(rota.obterIdVigia(), rota.obterData());
		docRota.append("localizacoes", new ArrayList<>());

		return Mono.from(collection.insertOne(docRota)).then();
	}

	public Mono<Rota> obterRotaPorId(IdRota id) {
		return Mono.from(collection.find(gerarIdRota(id.getIdVigia(), id.getData()))).map(docRota -> {
			var docId = docRota.get("_id", Document.class);

			var idRota = new IdRota();
			idRota.setIdVigia(docId.getString("idVigia"));
			idRota.setData(docId.getDate("data"));

			var rota = new Rota(idRota);

			@SuppressWarnings("unchecked")
			var localizacoes = (List<Document>) docRota.get("localizacoes");
			Localizacao localizacao = null;
			for (Document docLoc : localizacoes) {
				localizacao = new Localizacao();
				localizacao.setHora(docLoc.getDate("hora"));
				localizacao.setLatitude(docLoc.getDouble("latitude"));
				localizacao.setLongitude(docLoc.getDouble("longitude"));
				rota.add(localizacao);
			}
			return rota;
		});
	}

	private Document gerarIdRota(String idVigia, Date data) {
		var value = new Document().append("idVigia", idVigia).append("data", data);
		return new Document("_id", value);
	}
}
