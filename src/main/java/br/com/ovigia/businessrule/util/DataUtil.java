package br.com.ovigia.businessrule.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.dao.DataAccessResourceFailureException;

import br.com.ovigia.businessrule.exception.DataMalFormatadaException;

public class DataUtil {
	private static final String DATA_PATTERN = "dd-MM-yyyy";
	private static final DateFormat DATA_FORMATTER = new SimpleDateFormat(DATA_PATTERN);
	private static final DateFormat DATA_HORA_FORMATTER = new SimpleDateFormat("dd/MM/yyyyHH:mm");

	public static Date ajustarData(Date data) {
		var cal = Calendar.getInstance(TimeZone.getTimeZone("America/Sao_Paulo"));
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date gerarData() {
		return ajustarData(new Date());
	}

	public static Date parse(String data) throws DataMalFormatadaException {
		try {
			return DATA_FORMATTER.parse(data);
		} catch (ParseException e) {
			throw new DataAccessResourceFailureException(
					String.format("Data %s mal formatada. Ela deve estar no padrao %s", data, DATA_PATTERN), e);
		}
	}

	public static DataHora gerarDataHora(Date date) {
		var dataHora = DATA_HORA_FORMATTER.format(date);
		var data = dataHora.substring(0, 10);
		var hora = dataHora.substring(10);
		return new DataHora(data, hora);
	}
}
