package br.com.ovigia.businessrule.contrato.atualizar;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.repository.ContratoRepository;
import reactor.core.publisher.Mono;

public class AtualizarValorContratoRule implements BusinessRule<AtualizarValorContratoResquest, Void> {
    private ContratoRepository contratoRepository;

    public AtualizarValorContratoRule(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    @Override
    public Mono<Response<Void>> apply(AtualizarValorContratoResquest request) {
        return contratoRepository.atualizarValorContrato(request.idContrato, request.valor)
                .map(count -> Response.noContent());
    }

}
