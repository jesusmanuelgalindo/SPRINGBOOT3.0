package gob.jmas.utils;


import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Respuesta<Excepcion> handleException(Exception ex) {
        // Aquí puedes personalizar la estructura y el contenido de la respuesta de la excepción
        // Puedes usar tu excepción personalizada Excepcion si es necesario

        Excepcion excepcion = new Excepcion(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
        // Puedes generar una respuesta JSON, XML u otro formato según tus necesidades

        System.out.println("ERROR GlobalExceptionHandler handleException");
        return new Respuesta<Excepcion>(null,0, excepcion.getMessage());
    }
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public  Respuesta<Excepcion>handleInvalidDataAccessResourceUsageException(InvalidDataAccessResourceUsageException ex) {
        // Realiza el manejo de la excepción aquí
        Excepcion excepcion = new Excepcion(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());

        System.out.println("ERROR GlobalExceptionHandler handleInvalidDataAccessResourceUsageException");
        return new Respuesta<Excepcion>(null,0, excepcion.getMessage());
    }


    // Puedes agregar otros métodos handleException para manejar excepciones específicas según tus necesidades
    // Por ejemplo, handleNotFoundException, handleValidationException, etc.
}