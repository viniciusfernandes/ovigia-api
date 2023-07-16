package br.com.ovigia.model.repository;

import br.com.ovigia.model.Contrato;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface ContratoRepository {

    Mono<String> criarContrato(Contrato contrato);

    Flux<Contrato> obterIdContratosVencidos();

    Flux<Contrato> obterContratosAtivosByIdVigia(String idVigia);

    Mono<Contrato> obterContratoAtivoByIdCliente(String idCliente);

    Mono<Long> atualizarDataFimContrato(String idContrato, Date dataFim);

    Mono<Long> atualizarValorContrato(String idContrato, Double valor);

    Mono<Long> atualizarDataVencimentoContrato(String idContrato, Date dataVencimento);

}
