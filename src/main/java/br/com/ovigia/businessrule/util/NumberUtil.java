package br.com.ovigia.businessrule.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtil {
	private NumberUtil() {
	}

	public static double round(double valor, int escala) {
		return new BigDecimal(valor).setScale(escala, RoundingMode.HALF_UP).doubleValue();
	}

	public static double round(double valor) {
		return round(valor, 1);
	}

	public static double round2(double valor) {
		return round(valor, 2);
	}

	public static double round3(double valor) {
		return round(valor, 3);
	}
}
