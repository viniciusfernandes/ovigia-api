package br.com.ovigia.businessrule.contrato.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.businessrule.util.DataUtil;
import br.com.ovigia.businessrule.util.NumberUtil;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.ContratoRepository;
import br.com.ovigia.model.repository.MensalidadeRepository;
import br.com.ovigia.model.repository.VigiaRepository;
import reactor.core.publisher.Mono;

import java.util.Date;

public class ObterContratoAtivoClienteRule
        implements BusinessRule<ObterContratoAtivoClienteRequest, ObterContratoAtivoClienteResponse> {
    private ContratoRepository contratoRepository;
    private MensalidadeRepository mensalidadeRepository;
    private VigiaRepository vigiaRepository;

    public ObterContratoAtivoClienteRule(ContratoRepository contratoRepository, VigiaRepository vigiaRepository,
                                         MensalidadeRepository mensalidadeRepository) {
        this.contratoRepository = contratoRepository;
        this.vigiaRepository = vigiaRepository;
        this.mensalidadeRepository = mensalidadeRepository;
    }

    @Override
    public Mono<Response<ObterContratoAtivoClienteResponse>> apply(ObterContratoAtivoClienteRequest request) {
        return contratoRepository.obterContratoAtivoByIdCliente(request.idCliente).map(contrato -> {
                    var response = new ObterContratoAtivoClienteResponse();
                    response.id = contrato.id;
                    response.dataInicio = DataUtil.formatarData(contrato.dataInicio);
                    response.dataVencimento = DataUtil.formatarData(contrato.dataVencimento);
                    response.valor = contrato.valor;
                    response.idVigia = contrato.idVigia;
                    response.nomeVigia = contrato.nomeCliente;
                    return response;
                })
                .flatMap(response ->
                        mensalidadeRepository.obterMensalidadesbyIdContratoESituacaoInferiorADataMax(
                                        response.id,
                                        TipoSituacaoMensalidade.ABERTO, new Date())
                                .map(mensalidade -> {
                                    response.isContratoAtrasado = true;
                                    response.dataVencimento = DataUtil.formatarData(mensalidade.dataVencimento);
                                    return response;
                                })
                                .defaultIfEmpty(response))
                .flatMap(response -> vigiaRepository.obterNomeETelefoneEAvaliacao(response.idVigia)
                        .map(vigia -> {
                            response.nomeVigia = vigia.nome;
                            response.avaliacaoVigia = NumberUtil.round2(vigia.avaliacao.valor);
                            response.telefoneVigia = vigia.formatarTelefone();
                            return response;
                        }))
                .map(Response::ok)
                .switchIfEmpty(Mono.just(Response.noContent()));
    }

}
