package br.com.ovigia.model;

import br.com.ovigia.businessrule.util.DataUtil;

import java.util.Date;
import java.util.Objects;

public class IdFrequenciaRonda {
    public final String idCliente;
    public final Date dataRonda;

    public IdFrequenciaRonda(String idCliente, Date dataRonda) {
        this.idCliente = idCliente;
        this.dataRonda = DataUtil.ajustarData(dataRonda);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdFrequenciaRonda that = (IdFrequenciaRonda) o;
        return Objects.equals(idCliente, that.idCliente) && Objects.equals(dataRonda, that.dataRonda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, dataRonda);
    }
}
