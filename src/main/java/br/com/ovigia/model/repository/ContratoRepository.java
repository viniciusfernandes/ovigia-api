package br.com.ovigia.model.repository;

import static br.com.ovigia.repository.parser.ContratoParser.fromDoc;
import static br.com.ovigia.repository.parser.ContratoParser.toDoc;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.bson.Document;

import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;

import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.IdsContrato;
import br.com.ovigia.model.enumeration.TipoSituacaoContrato;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContratoRepository {

    Mono<String> criarContrato(Contrato contrato);

    Flux<Contrato> obterIdContratosVencidos();

    Flux<Contrato> obterContratosAtivosByIdVigia(String idVigia);

    Mono<Contrato> obterContratoAtivoByIdCliente(String idCliente);

    Mono<Long> atualizarDataFimContrato(String idContrato, Date dataFim);

    Mono<Long> atualizarValorContrato(String idContrato, Double valor);

    Mono<Long> atualizarDataVencimentoContrato(String idContrato, Date dataVencimento);

    Mono<IdsContrato> obterIdVigiaByIdContrato(String idContrato);

}
