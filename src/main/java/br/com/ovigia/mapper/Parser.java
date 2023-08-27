package br.com.ovigia.mapper;

public interface Parser<R, T> {
    default T parse(R source) {
        Validator.validate(source);
        return map(source);
    }

    T map(R source);
}
