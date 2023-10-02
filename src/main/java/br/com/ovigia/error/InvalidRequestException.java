package br.com.ovigia.error;

public class InvalidRequestException extends RuntimeException {


    public InvalidRequestException(String mensagem) {
        super(mensagem);
    }
}
