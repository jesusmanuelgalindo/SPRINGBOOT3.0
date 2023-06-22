package gob.jmas.service.facturama;
import gob.jmas.dto.facturamaFactura.FacturamaFactura;
import gob.jmas.dto.facturamaResponse.FacturamaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FacturamaService {

    private final WebClient webClient;

    public FacturamaService(@Value("${facturama.api.base-url}") String baseUrl,
                            @Value("${facturama.api.api-key}") String apiKey) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Basic " + apiKey)
                .build();
    }

    public Mono<FacturamaResponse> enviarFactura(FacturamaFactura facturamaFactura) {
        return webClient.post()
                .uri("/2/cfdis")
                .bodyValue(facturamaFactura)
                .retrieve()
                .bodyToMono(FacturamaResponse.class);
    }
}
