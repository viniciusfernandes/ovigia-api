
package br.com.ovigia.repository.impl.hash;

import br.com.ovigia.model.Mensalidade;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.MensalidadeRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MensalidadeHashRepository implements MensalidadeRepository {
    private final Map<String, Mensalidade> table = new HashMap<>();

    public Mono<Mensalidade> criarMensalidade(Mensalidade mensalidade) {
        mensalidade.id = UUID.randomUUID().toString();
        table.put(mensalidade.id, mensalidade);
        return Mono.just(mensalidade);
    }

    public Mono<Long> atualizaDataPagamentoMensalidade(String idMensalidade, Date dataPagamento,
                                                       TipoSituacaoMensalidade situacao) {
        var mens = table.get(idMensalidade);
        if (mens == null) {
            return Mono.empty();
        }
        mens.dataPagamento = dataPagamento;
        mens.situacao = situacao;
        return Mono.just(1L);
    }

    public Flux<Mensalidade> obterMensalidadesDataVencimentoInferiorByIdVigia(String idVigia, Date dataLimite,
                                                                              TipoSituacaoMensalidade situacao) {
        return Flux.fromIterable(table.values()).filter(mens ->
                mens.idVigia.equals(idVigia) &&
                        mens.dataVencimento.before(dataLimite) &&
                        mens.dataPagamento == null &&
                        mens.situacao == situacao);
    }

    @Override
    public Mono<Mensalidade> obterMensalidadesbyIdContratoESituacaoInferiorADataMax(String idContrato, TipoSituacaoMensalidade situacao, Date dataMaxima) {
        return Mono.empty();
    }

}
