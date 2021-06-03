package br.com.ovigia.error;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

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
		error.mensagem = "Falha interna no servidor. Procure a equipe de desenvolvimento caso o problema persistir.";
		error.url = (String) map.get("path");
		error.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		error.tipo = ex.getClass().getSimpleName();

		map.put("error", error);

		logger.error("Erro global", ex);
		return map;
	}
}

class GlobalError {
	public String timestamp;
	public String url;
	public int status;
	public String tipo;
	public String mensagem;
}