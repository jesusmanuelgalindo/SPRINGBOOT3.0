package gob.jmas.dto.facturamaFactura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GlobalInformation {

    @Size(min = 2, max = 2)
    private String Periodicity;

    @Size(min = 2, max = 2)
    private String Months;

    @Size(min = 4, max = 4)
    private String Year;
}
