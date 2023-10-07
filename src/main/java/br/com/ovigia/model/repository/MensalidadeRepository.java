
package br.com.ovigia.model.repository;

import br.com.ovigia.model.Mensalidade;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

public interface MensalidadeRepository {

    Mono<Mensalidade> criarMensalidade(Mensalidade mensalidade);

    Mono<Long> atualizaDataPagamentoMensalidade(String idMensalidade, Date dataPagamento,
                                                TipoSituacaoMensalidade situacao);

    Flux<Mensalidade> obterMensalidadesDataVencimentoInferiorByIdVigia(String idVigia, Date dataLimite,
                                                                       TipoSituacaoMensalidade situacao);

    Mono<Mensalidade> obterMensalidadesbyIdContratoESituacaoInferiorADataMax(String idContrato, TipoSituacaoMensalidade situacao,
                                                                             Date dataMaxima);
}
