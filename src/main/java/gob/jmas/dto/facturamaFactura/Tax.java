package gob.jmas.dto.facturamaFactura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Tax {
    //Monto total del impuesto
    @Pattern(regexp = "^\\d+(?:\\.\\d{1,2})?$")
    private BigDecimal Total;

    //Nombre del Impuesto
    @Pattern(regexp = "^(IVA|ISR|IEPS|IVA RET|IVA Exento)$")
    private String Name;

    //Monto base al que se aplica el impuesto
    @DecimalMin(value = "1E-06")
    private BigDecimal Base;

    //Porcentaje de impuesto
    @DecimalMin(value = "0.0")
    private BigDecimal Rate;

    //Especifica si es una retención
    private Boolean IsRetention;

    //Especifica si es un impuesto federal
    private Boolean IsFederalTax;

    //Especifica si es el impuesto es Cuota, si no se toma como Tasa
    private Boolean IsQuota;

}