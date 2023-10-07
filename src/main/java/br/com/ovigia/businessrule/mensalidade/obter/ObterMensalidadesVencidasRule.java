package br.com.ovigia.businessrule.mensalidade.obter;

import br.com.ovigia.businessrule.BusinessRule;
import br.com.ovigia.businessrule.Response;
import br.com.ovigia.model.enumeration.TipoSituacaoMensalidade;
import br.com.ovigia.model.repository.MensalidadeRepository;
import reactor.core.publisher.Mono;

import java.util.List;

import static br.com.ovigia.businessrule.util.DataUtil.ajustarData;
import static br.com.ovigia.businessrule.util.DataUtil.calcularDiferencaDias;
import static br.com.ovigia.businessrule.util.DataUtil.formatarData;

public class ObterMensalidadesVencidasRule
        implements BusinessRule<ObterMensalidadesVencidasRequest, List<ObterMensalidadesVencidasResponse>> {
    private MensalidadeRepository mensalidadeRepository;

    public ObterMensalidadesVencidasRule(MensalidadeRepository mensalidadeRepository) {
        this.mensalidadeRepository = mensalidadeRepository;
    }

    @Override
    public Mono<Response<List<ObterMensalidadesVencidasResponse>>> apply(ObterMensalidadesVencidasRequest request) {
        var dataAtual = ajustarData();
        return mensalidadeRepository.obterMensalidadesDataVencimentoInferiorByIdVigia(request.idVigia, dataAtual,
                TipoSituacaoMensalidade.ABERTO).map(mensalidade -> {
            var response = new ObterMensalidadesVencidasResponse();
            response.dataVencimento = formatarData(mensalidade.dataVencimento);
            response.id = mensalidade.id;
            response.nomeCliente = mensalidade.nomeCliente;
            response.telefoneCliente = mensalidade.telefoneCliente;
            response.valor = mensalidade.valor;
            response.isMensalidadeAtrasada = mensalidade.dataVencimento.compareTo(dataAtual) < 0;
            return response;
        }).collectList().map(Response::ok);
    }

}
