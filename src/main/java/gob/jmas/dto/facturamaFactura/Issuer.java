package gob.jmas.dto.facturamaFactura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Issuer {
    private String FiscalRegime;
    private String ComercialName;
    private String Rfc;
    private String TaxName;
    private String Email;
    private String Phone;
    private Address TaxAddress;
    private Address IssuedIn;
    private String UrlLogo;
}
