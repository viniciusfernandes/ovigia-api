package br.com.ovigia.repository.parser;

import org.bson.Document;

import br.com.ovigia.model.Contrato;
import br.com.ovigia.model.enumeration.TipoSituacaoContrato;

public class ContratoParser {
	private ContratoParser() {
	}

	public static Document toDoc(Contrato contrato) {
		return new Document("_id", contrato.id).append("idCliente", contrato.idCliente)
				.append("idVigia", contrato.idVigia).append("dataInicio", contrato.dataInicio)
				.append("dataFim", contrato.dataFim).append("valor", contrato.valor)
				.append("dataVencimento", contrato.dataVencimento).append("nomeCliente", contrato.nomeCliente)
				.append("telefoneCliente", contrato.telefoneCliente).append("situacao", contrato.situacao.toString());
	}

	public static Contrato fromDoc(Document doc) {
		var contrato = new Contrato();
		contrato.id = doc.getString("_id");
		contrato.idCliente = doc.getString("idCliente");
		contrato.dataInicio = doc.getDate("dataInicio");
		contrato.dataFim = doc.getDate("dataFim");
		contrato.idVigia = doc.getString("idVigia");
		contrato.valor = doc.getDouble("valor");
		contrato.dataVencimento = doc.getDate("dataVencimento");
		contrato.nomeCliente = doc.getString("nomeCliente");
		contrato.telefoneCliente = doc.getString("telefoneCliente");
		var situacao = doc.getString("situacao");
		if (situacao != null) {
			contrato.situacao = TipoSituacaoContrato.valueOf(situacao);
		}
		return contrato;
	}

}
