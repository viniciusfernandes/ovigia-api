package br.com.ovigia.error;

import br.com.ovigia.businessrule.exception.AutenticacaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ResponseStatusException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

	@Override
	public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {

		Map<String, Object> map = super.getErrorAttributes(request, options);

		Throwable ex = getError(request);

		GlobalError error = new GlobalError();
		error.timestamp = df.format((Date) map.get("timestamp"));
		error.url = (String) map.get("path");
		error.status = (int) map.get("status");
		if (ex instanceof ResponseStatusException) {
			error.mensagem = "Recurso nao existente";
			error.status = ((ResponseStatusException) ex).getStatus().value();
		} else if (ex instanceof AutenticacaoException) {
			error.mensagem = "Usuario nao autenticado.";
			error.tipo = ex.getClass().getSimpleName();
			error.status = HttpStatus.UNAUTHORIZED.value();
		} else if (error.status == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			error.mensagem = "Falha interna no servidor. Procure a equipe de desenvolvimento caso o problema persistir.";
			error.tipo = ex.getClass().getSimpleName();
		}

		map.put("error", error);

		logger.error("Erro global", ex);
		return map;
	}
}

