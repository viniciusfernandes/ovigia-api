package br.com.ovigia.model;

import java.util.Date;
import java.util.Objects;

public class IdRonda {
    public final String idVigia;
    public final Date dataRonda;

    public IdRonda(String idVigia, Date dataRonda) {
        this.idVigia = idVigia;
        this.dataRonda = dataRonda;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdRonda idRonda = (IdRonda) o;
        return Objects.equals(idVigia, idRonda.idVigia) && Objects.equals(dataRonda, idRonda.dataRonda);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idVigia, dataRonda);
    }
}
