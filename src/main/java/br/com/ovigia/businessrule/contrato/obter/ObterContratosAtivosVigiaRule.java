package br.com.ovigia.businessrule.contrato.obter;

import java.util.List;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.SolicitacaoVisita;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.SolicitacaoVisitaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ObterContratosAtivosVigiaRule
        implements BusinessRule<ObterContratosAtivosVigiaRequest, List<ObterContratosAtivosVigiaResponse>> {
    private ContratoRepository contratoRepository;
    private SolicitacaoVisitaRepository solicitacaoRepository;

    public ObterContratosAtivosVigiaRule(ContratoRepository contratoRepository,
                                         SolicitacaoVisitaRepository solicitacaoRepository) {
        this.contratoRepository = contratoRepository;
        this.solicitacaoRepository = solicitacaoRepository;
    }

    @Override
    public Mono<Response<List<ObterContratosAtivosVigiaResponse>>> apply(ObterContratosAtivosVigiaRequest request) {
        var obterSolicitacao = solicitacaoRepository.obterSolicitacaoByIdVigia(request.idVigia)
                .map(SolicitacaoVisita::toContrato);
        var obterContratos = contratoRepository.obterContratosAtivosByIdVigia(request.idVigia)
                .sort((c1, c2) -> {
                    if ((c1.id == null && c2.id != null) || (c1.id != null && c2.id == null)
                            || (c1.id == null && c2.id == null)) {
                        return -1;
                    }
                    return c1.nomeCliente.compareTo(c2.nomeCliente);
                });

        return Flux.concat(obterSolicitacao, obterContratos)
                .map(contrato -> {
                    var response = new ObterContratosAtivosVigiaResponse();
                    response.id = contrato.id;
                    response.dataInicio = DataUtil.formatarData(contrato.dataInicio);
                    response.dataVencimento = DataUtil.formatarData(contrato.dataVencimento);
                    response.valor = contrato.valor;
                    response.idCliente = contrato.idCliente;
                    response.nomeCliente = contrato.nomeCliente;
                    response.telefoneCliente = contrato.telefoneCliente;
                    response.isVencido = contrato.isContratoVencido();
                    return response;
                }).collectList().map(Response::ok).switchIfEmpty(Mono.just(Response.noContent()));
    }

}
