
package br.com.ovigia.model.repository;

import br.com.ovigia.model.Faturamento;
import br.com.ovigia.model.IdFaturamento;
import reactor.core.publisher.Mono;

public interface FaturamentoRepository {

    Mono<Faturamento> criarFaturamento(Faturamento faturamento);

    Mono<Long> atualizarFaturamento(Faturamento faturamento);

    Mono<Faturamento> obterValoresFaturamento(IdFaturamento id);

}
