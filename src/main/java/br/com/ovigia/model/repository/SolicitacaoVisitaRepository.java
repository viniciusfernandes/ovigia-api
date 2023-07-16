package br.com.ovigia.model.repository;

import br.com.ovigia.model.SolicitacaoVisita;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SolicitacaoVisitaRepository {

    Mono<SolicitacaoVisita> criarSolicitacao(SolicitacaoVisita solicitacao);

    Flux<SolicitacaoVisita> obterSolicitacaoByIdVigia(String idVigia);

    Mono<String> obterIdVigiaSolicitado(String idCliente);

    Mono<Long> removerSolicitacao(String idCliente);

}
