package gob.jmas.utils;

import gob.jmas.dto.ReceptorDto;
import gob.jmas.utils.Respuesta;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        // Obtén los mensajes de error de las violaciones de restricción
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());





        // Devuelve una respuesta con el cuerpo personalizado y el código de estado 400 (Bad Request)
        return ResponseEntity.badRequest().body(new Respuesta<String>(null,0,String.join(System.lineSeparator(), errors)));
     //   return handleExceptionInternal(ex, respuesta, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}