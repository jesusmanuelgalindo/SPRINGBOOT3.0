package gob.jmas.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ConstraintViolation;

@ControllerAdvice
public class GlobalExceptionHandler {
    //ConstraintViolationException

    @Autowired
    private HttpServletRequest request;
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Respuesta<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        //Se lanza cuando se viola una restricción de validación en una entidad o DTO
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("Error desconocido");

        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);

        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Respuesta<String>> handleBindException(BindException ex) {
        //se produce cuando hay errores de enlace de datos durante la validación de formularios en Spring.
        //Esta excepción se lanza cuando no se puede realizar el enlace de los datos recibidos en la solicitud a los objetos de dominio correspondientes.
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElseGet(() -> "Error desconocido");

        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);
        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Respuesta<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        //se produce cuando falla la validación de los argumentos de un método anotado con @Valid en un controlador de Spring
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElseGet(() -> "Error desconocido");

        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);
        return ResponseEntity.badRequest().body(respuesta);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Respuesta<String>> handleRuntimeException(RuntimeException ex) {
        //Clase base para todas las excepciones no verificadas en Java.
        //Puede ser generadas por errores de lógica del programa, problemas de programación, condiciones inesperadas o situaciones excepcionales.
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = ex.getMessage();
        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Respuesta<String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        //Se produce cuando no se encuentra ningún controlador (handler) adecuado para la solicitud realizada.
        //Esto puede ocurrir cuando se solicita una URL que no coincide con ninguna ruta definida en la aplicación.
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = "El endpoint solicitado no existe.";
        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Respuesta<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        //Se lanza cuando no se puede leer o deserializar correctamente el cuerpo (payload) de la solicitud HTTP.
        //Puede ocurrir cuando el cuerpo de la solicitud está en un formato incorrecto o no cumple con el tipo de datos esperado.
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = ex.getMessage();
        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);
        return ResponseEntity.badRequest().body(respuesta);
    }
}
