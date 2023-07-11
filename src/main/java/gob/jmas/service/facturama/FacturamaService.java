package gob.jmas.service.facturama;
import com.fasterxml.jackson.databind.ObjectMapper;
import gob.jmas.dto.facturamaException.FacturamaException;
import gob.jmas.dto.facturamaException.ModelException;
import gob.jmas.dto.facturamaFactura.Cfdi;
import gob.jmas.dto.facturamaResponse.FacturamaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Map;

@Service
public class FacturamaService {

    private final WebClient webClient;

    public FacturamaService(@Value("${facturama.api.base-url}") String baseUrl,
                            @Value("${facturama.api.usuario}") String usuario,
                            @Value("${facturama.api.password}") String password) {
        String apiKey = encodeCredentials(usuario, password);

        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Basic " + apiKey)
                .build();
    }

    private String encodeCredentials(String usuario, String password) {
        String credentials = usuario + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }


//public Mono<Map<String, Object>> enviarFactura(Cfdi cfdi) {
//    ObjectMapper objectMapper = new ObjectMapper();
//
//    ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
//            .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(
//                    new Jackson2JsonDecoder(objectMapper)
//            ))
//            .build();
//
//    WebClient customWebClient = webClient.mutate()
//            .exchangeStrategies(exchangeStrategies)
//            .build();
//
//    return customWebClient.post()
//            .uri("/3/cfdis")
//            .bodyValue(cfdi)
//            .retrieve()
//            .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
//}



    public Mono<Map<String, Object>> enviarFactura(Cfdi cfdi) {
        ObjectMapper objectMapper = new ObjectMapper();

        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().jackson2JsonDecoder(
                        new Jackson2JsonDecoder(objectMapper)
                ))
                .build();

        WebClient customWebClient = webClient.mutate()
                .exchangeStrategies(exchangeStrategies)
                .filter(logRequest()) // Agregar filtro para registrar las cabeceras de la solicitud
                .filter(logResponse()) // Agregar filtro para registrar las cabeceras de la respuesta
                .build();

        return customWebClient.post()
                .uri("/3/cfdis")
                .bodyValue(cfdi)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .onErrorMap(this::handleError); // Capturar y manejar la excepción WebClientResponseException
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            System.out.println("Request Headers: " + clientRequest.headers());
            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            System.out.println("Response Headers: " + clientResponse.headers());
            return Mono.just(clientResponse);
        });
    }

    private Throwable handleError(Throwable throwable) {
        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException responseException = (WebClientResponseException) throwable;
            System.out.println("Error Response Body: " + responseException.getResponseBodyAsString());
        }
        return throwable;
    }




}
