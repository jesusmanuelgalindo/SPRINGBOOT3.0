package gob.jmas.dto.facturamaResponse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private double discount;
    private double quantity;
    private String unit;
    private String description;
    private double unitValue;
    private double total;

    // Getter y Setter
}