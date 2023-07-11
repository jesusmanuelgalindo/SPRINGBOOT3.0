package gob.jmas.dto.facturamaFactura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Receiver {
    //Atributo para indicar el Id del Cliente a relacionar
    private Integer Id;

    //Atributo requerido para precisar la Clave del Registro Federal de Contribuyentes correspondiente al contribuyente
    @Pattern(regexp = "[A-Z&Ñ]{3,4}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]")
    private String Rfc;

    //Nombre del cliente Requerido
    @Pattern(regexp = "[^|]{1,300}")
    @Size(min = 1, max = 300)
    private String Name;

    //Régimen fiscal, tal como está dado de alta en el SAT
    private String FiscalRegime;

    //Debe ser de acuerdo al régimen fiscal del receptor y desaparece el P01
    @Pattern(regexp = "G01|G02|G03|I01|I02|I03|I04|I05|I06|I07|I08|D01|D02|D03|D04|D05|D06|D07|D08|D09|D10")
    private String CfdiUse;

    //Código postal del receptor
    private String TaxZipCode;

    //Detalles de la Dirección
    //Al parecer descontinuado
    //private Address Address;
    //   private String Email;



}