package gob.jmas.dto.facturamaFactura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    //Id de la Branch Office
    private Integer Id;

    //Calle
    @Size(min = 1, max = 100)
    private String Street;

    //Numero Exterior
    @Size(min = 1, max = 30)
    private String ExteriorNumber;

    //Numero Interior
    @Size(min = 0, max = 30)
    private String InteriorNumber;

    //Colonia
    @Size(min = 0, max = 80)
    private String Neighborhood;

    //ZipCode
    @Size(min = 0, max = 5)
    private String ZipCode;

    //Localidad (En facturama web a veces utilizado como ciudad)
    @Size(min = 0, max = 80)
    private String Locality;

    //Municipio
    @Size(min = 0, max = 100)
    private String Municipality;

    //Estado
    @Size(min = 1, max = 50)
    private String State;

    //Pais
    @Size(min = 1, max = 50)
    private String Country;
}
