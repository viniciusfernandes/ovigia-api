package br.com.ovigia.businessrule.util;

import br.com.ovigia.businessrule.exception.DataMalFormatadaException;
import org.springframework.dao.DataAccessResourceFailureException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DataUtil {
	private static final String DATA_PATTERN = "dd-MM-yyyy";
	private static final String DATA_PATTERN_SLASH = "dd/MM/yyyy";
	private static final DateFormat DATA_FORMATTER = new SimpleDateFormat(DATA_PATTERN);
	private static final DateFormat DATA_FORMATTER_SLASH = new SimpleDateFormat(DATA_PATTERN_SLASH);

	private static final DateFormat DATA_HORA_FORMATTER = new SimpleDateFormat("dd/MM/yyyyHH:mm");
	private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("America/Sao_Paulo");

	public static Date ajustarData(Date data) {
		if (data == null) {
			return null;
		}
		var cal = Calendar.getInstance(TIME_ZONE);
		ajustarCaledar(cal);
		return cal.getTime();
	}

	public static Date ajustarData() {
		return ajustarData(new Date());
	}

	public static String formatarData(Date data) {
		if (data == null) {
			return null;
		}
		return DATA_FORMATTER_SLASH.format(data);
	}

	public static Date parse(String data) throws DataMalFormatadaException {
		try {
			return DATA_FORMATTER.parse(data);
		} catch (ParseException e) {
			throw new DataAccessResourceFailureException(
					String.format("Data %s mal formatada. Ela deve estar no padrao %s", data, DATA_PATTERN), e);
		}
	}

	public static DataHora obterDataHora(Date date) {
		var dataHora = DATA_HORA_FORMATTER.format(date);
		var data = dataHora.substring(0, 10);
		var hora = dataHora.substring(10);
		return new DataHora(data, hora);
	}

	public static DataHora obterDataHora() {
		return obterDataHora(new Date());
	}

	public static DiaMes obterDiaMes(Date data) {
		var cal = Calendar.getInstance(TIME_ZONE);
		cal.setTime(data);
		return new DiaMes(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1);
	}

	public static MesAno obterMesAno(Date data) {
		var cal = Calendar.getInstance(TIME_ZONE);
		cal.setTime(data);
		return new MesAno(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
	}

	public static MesAno obterMesAno() {
		return obterMesAno(new Date());
	}

	public static DiaMes obterDiaMes() {
		return obterDiaMes(new Date());
	}

	public static Date ajustarDataProximoMes(Date data) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(data);
		cal.add(Calendar.MONTH, 1);
		ajustarCaledar(cal);
		return cal.getTime();
	}

	public static Date ajustarDataProximoMes() {
		return ajustarDataProximoMes(new Date());
	}

	public static String formatarDataByDia(Integer diaMes) {
		return formatarData(gerarDataByDia(diaMes));
	}

	public static Date gerarDataByDia(Integer diaMes) {
		var cal = Calendar.getInstance(TIME_ZONE);
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, diaMes);
		return cal.getTime();
	}

	private static void ajustarCaledar(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}
}
