
package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.RondaParser.toDoc;
import static br.com.ovigia.repository.parser.RondaParser.toIdDoc;

import java.util.Arrays;
import java.util.Date;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Id;
import br.com.ovigia.model.Localizacao;
import br.com.ovigia.model.Ronda;
import br.com.ovigia.repository.parser.LocalizacaoParser;
import br.com.ovigia.repository.parser.RondaParser;
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

	public Mono<Void> criarRonda(Ronda ronda) {
		var docRota = toDoc(ronda);
		return Mono.from(collection.insertOne(docRota)).then();
	}

	public Mono<Ronda> obterRondaPorId(Id id) {
		return Mono.from(collection.find(toIdDoc(id))).map(docRonda -> RondaParser.fromDoc(docRonda));
	}

	public Mono<Double> obterDistanciaRonda(Id id) {
		var match = new Document("$match", toIdDoc(id));
		var project = new Document("$project", new Document("distancia", 1));
		var pipeline = Arrays.asList(match, project);

		return Mono.from(collection.aggregate(pipeline)).flatMap(doc -> {
			var distancia = doc.getDouble("distancia");
			if (distancia == null) {
				return Mono.empty();
			}
			return Mono.just(distancia);
		});
	}

	public Mono<Void> concatenarRonda(Ronda ronda) {
		var each = new Document("$each", LocalizacaoParser.toDoc(ronda.localizacoes));
		var docLocalizacoes = new Document("localizacoes", each);
		var fields = new Document("fim", ronda.fim).append("distancia", ronda.distancia);
		var update = new Document("$push", docLocalizacoes).append("$set", fields);
		return Mono.from(collection.updateOne(toIdDoc(ronda.id), update)).then();
	}

	public Mono<Ronda> obterUltimaDataRonda(String idvigia) {
		var match = new Document("$match", new Document("_id.idVigia", idvigia));
		var fields = new Document("_id.data", 1).append("inicio", 1).append("fim", 1);
		var project = new Document("$project", fields);
		var pipeline = Arrays.asList(match, project);
		return Mono.from(collection.aggregate(pipeline)).map(docRonda -> new Ronda());
	}

}
