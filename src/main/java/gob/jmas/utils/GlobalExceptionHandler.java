package gob.jmas.utils;

import ch.qos.logback.core.joran.spi.ElementSelector;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import gob.jmas.dto.facturamaException.FacturamaException;
import gob.jmas.dto.facturamaException.ModelException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ConstraintViolation;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    //ConstraintViolationException
    private static final Logger logger = LoggerFactory.getLogger(Respuesta.class);
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private Convertir convertir;
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Respuesta<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        logger.warn("[ConstraintViolationException] "+ex.getMessage());
        logger.warn("[ConstraintViolationException] "+rootCause.getMessage());
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
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        logger.error("[BindException] "+ex.getMessage());
        logger.error("[BindException] "+rootCause.getMessage());
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
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Respuesta<String>> handleWebClientResponseException(WebClientResponseException ex, HttpServletRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        logger.error("[WebClientResponseException] " + ex.getMessage());
        //logger.error("[WebClientResponseException] " + rootCause.getMessage());

        String nombreDelEndpoint = request.getRequestURI();
        String errorMessage = ex.getResponseBodyAsString();
        ModelException modelException = convertir.jsonAModelo(errorMessage,ModelException.class);
        //System.out.println(convertir.objetoAJsonString(modelException));

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String[]> entry : modelException.getModelState().entrySet()) {
            String clave = entry.getKey();
            String[] valores = entry.getValue();
            for (String valor : valores) {
                stringBuilder.append(clave).append(":").append(valor).append("\n");
            }
        }
        Respuesta<String> respuesta = new Respuesta<>(null, 0, modelException.getMessage()+"! "+stringBuilder.toString(), nombreDelEndpoint);
        return ResponseEntity.status(ex.getStatusCode()).body(respuesta);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Respuesta<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        logger.warn("[MethodArgumentNotValidException] "+ex.getMessage());
        logger.warn("[MethodArgumentNotValidException] "+rootCause.getMessage());
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
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        logger.error("[RuntimeException] "+ex.getMessage());
        logger.error("[RuntimeException] "+rootCause.getMessage());
        //Clase base para todas las excepciones no verificadas en Java.
        //Puede ser generadas por errores de lógica del programa, problemas de programación, condiciones inesperadas o situaciones excepcionales.
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = ex.getMessage();
        System.out.println("RUNTIME EXCEPTION");
        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Respuesta<String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        logger.error("[NoHandlerFoundException] "+ex.getMessage());
        logger.error("[NoHandlerFoundException] "+rootCause.getMessage());
        //Se produce cuando no se encuentra ningún controlador (handler) adecuado para la solicitud realizada.
        //Esto puede ocurrir cuando se solicita una URL que no coincide con ninguna ruta definida en la aplicación.
        String nombreDelEndpoint=request.getRequestURI();
        String errorMessage = "El endpoint solicitado no existe.";
        Respuesta<String> respuesta = new Respuesta<String>(null,0,errorMessage,nombreDelEndpoint);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Respuesta<String>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        logger.warn("[HttpMessageNotReadableException] " + ex.getMessage());
        logger.warn("[HttpMessageNotReadableException] " + rootCause.getMessage());

        if (rootCause instanceof JsonMappingException) {
            JsonMappingException jsonMappingException = (JsonMappingException) rootCause;
            String fieldName = jsonMappingException.getPath().get(0).getFieldName();
            String errorMessage = "La información enviada para el campo '" + fieldName + "' no cumple con el formato establecido";
            Respuesta<String> respuesta = new Respuesta<>(null, 0, errorMessage, request.getRequestURI());
            return ResponseEntity.badRequest().body(respuesta);
        }

        String errorMessage = "La información enviada no cumple con el formato establecido";
        Respuesta<String> respuesta = new Respuesta<>(null, 0, errorMessage, request.getRequestURI());
        return ResponseEntity.badRequest().body(respuesta);
    }


//    @ExceptionHandler(NestedRuntimeException.class)
//    public ResponseEntity<Respuesta<String>> handleNestedRuntimeException(NestedRuntimeException ex) {
//        Throwable rootCause = ExceptionUtils.getRootCause(ex);
//        logger.error("[NestedRuntimeException] "+ex.getMessage());
//        logger.error("[NestedRuntimeException] "+rootCause.getMessage());
//            // Lógica para manejar la excepción SQLGrammarException
//            String nombreDelEndpoint = request.getRequestURI();
//            String errorMessage = "Error al ejecutar la consulta en el servidor de Bases de datos";
//
//            Respuesta<String> respuesta = new Respuesta<String>(null, 0, errorMessage, nombreDelEndpoint);
//            return ResponseEntity.internalServerError().body(respuesta);
//
//    }

//
//    @ExceptionHandler(InvalidFormatException.class)
//    public ResponseEntity<Respuesta<String>> handleInvalidFormatException(InvalidFormatException ex) {
//        Throwable rootCause = ExceptionUtils.getRootCause(ex);
//        logger.error("[InvalidFormatException] "+ex.getMessage());
//        logger.error("[InvalidFormatException] "+rootCause.getMessage());
//        String errorMessage = "Error al deserializar un valor. Tipo de dato incorrecto.";
//
//        Respuesta<String> respuesta = new Respuesta<>(null, 0, errorMessage, null);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
//    }
//
//    @ExceptionHandler(JsonMappingException.class)
//    public ResponseEntity<Respuesta<String>> handleJsonMappingException(JsonMappingException ex) {
//        Throwable rootCause = ExceptionUtils.getRootCause(ex);
//        logger.error("[JsonMappingException] "+ex.getMessage());
//        logger.error("[JsonMappingException] "+rootCause.getMessage());
//        String fieldName = ex.getPath().get(0).getFieldName();
//        String errorMessage = fieldName + " debe ser un valor válido de tipo Integer.";
//        Respuesta<String> respuesta = new Respuesta<>(null, 0, errorMessage, null);
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
//    }
}
