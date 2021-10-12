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
	public static String dataRotaPadrao = "dd-MM-yyyy";
	private static DateFormat format = new SimpleDateFormat(dataRotaPadrao);

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

	public static Date parseToDataRota(String data) throws DataMalFormatadaException {
		Date dataRota;
		try {
			dataRota = format.parse(data);
		} catch (ParseException e) {
			throw new DataAccessResourceFailureException(
					String.format("Data %s mal formatada. Ela deve estar no padrao %s", data, dataRotaPadrao), e);
		}
		return ajustarData(dataRota);
	}

}
