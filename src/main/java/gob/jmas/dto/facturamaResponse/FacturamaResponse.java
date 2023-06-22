package gob.jmas.dto.facturamaResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FacturamaResponse {
    private String id;
    private String cfdiType;
    private String serie;
    private String folio;
    private LocalDateTime date;
    private String paymentTerms;
    private String paymentConditions;
    private String paymentMethod;
    private String expeditionPlace;
    private double exchangeRate;
    private String currency;
    private double subtotal;
    private double discount;
    private double total;
    private String observations;
    private Issuer issuer;
    private Receiver receiver;
    private List<Item> items;
    private List<Tax> taxes;
    private Complement complement;


}








