package gob.jmas.utils;

import org.springframework.http.HttpStatus;

public class Excepcion extends RuntimeException{
    private HttpStatus tipo;

    public Excepcion(HttpStatus tipo, String mensaje) {
        super(mensaje);
        this.tipo = tipo;
    }

    public HttpStatus getTipo() {
        return tipo;
    }

}
