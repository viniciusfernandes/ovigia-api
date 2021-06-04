package br.com.ovigia.repository;

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

	public Mono<Void> criar(String idVigia, Date data, Localizacao localizacao) {
		var idRota = gerarIdRota(idVigia, data);
		var doclocalizacao = new Document();
		doclocalizacao.append("hora", localizacao.getHora());
		doclocalizacao.append("latitude", localizacao.getLatitude());
		doclocalizacao.append("longitude", localizacao.getLongitude());

		var pushLocalizacao = new Document("$push", new Document("localizacoes", doclocalizacao));

		return Mono.from(collection.updateOne(idRota, pushLocalizacao)).flatMap(res -> Mono.empty());
	}

	public Mono<Rota> buscarPorIdVigia(String idVigia, Date data) {
		return Mono.from(collection.find(new Document("_id", gerarIdRota(idVigia, data)))).map(docRota -> {
			var rota = new Rota();
			var idRota = new IdRota();
			idRota.setIdVigia(docRota.getString("idVigia"));
			idRota.setData(docRota.getDate("data"));

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
		return new Document("_id", new Document().append("idVigia", idVigia).append("data", data));
	}
}
