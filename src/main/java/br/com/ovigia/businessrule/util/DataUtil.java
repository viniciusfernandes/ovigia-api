package br.com.ovigia.businessrule.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.dao.DataAccessResourceFailureException;

import br.com.ovigia.businessrule.exception.DataRotaMalFormatadaException;

public class DataUtil {
	public static String dataRotaPadrao = "dd-MM-yyyy";
	private static DateFormat format = new SimpleDateFormat(dataRotaPadrao);

	public static Date gerarData(Date data) {
		var cal = Calendar.getInstance();
		cal.setTime(data);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date parseToDataRota(String data) throws DataRotaMalFormatadaException {
		Date dataRota;
		try {
			dataRota = format.parse(data);
		} catch (ParseException e) {
			throw new DataAccessResourceFailureException(
					String.format("Data %s mal formatada. Ela deve estar no padrao %s", data, dataRotaPadrao), e);
		}
		return gerarData(dataRota);
	}

}
