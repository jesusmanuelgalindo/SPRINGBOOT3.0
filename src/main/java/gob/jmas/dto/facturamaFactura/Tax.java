package gob.jmas.dto.facturamaFactura;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Tax {
    private double Total;
    private String Name;
    private double Base;
    private double Rate;
    private boolean IsRetention;

    // Constructor, getters y setters
}