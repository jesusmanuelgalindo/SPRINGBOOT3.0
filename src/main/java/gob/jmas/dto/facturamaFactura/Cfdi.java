package gob.jmas.dto.facturamaFactura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import javax.validation.constraints.*;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cfdi {






    //Nombre que se mostrará en el PDF, tiene finalidad ilustrativa, para que rápidamente el receptor conozca a lo que se refiere el comprobante.
    private Integer NameId;

    //El efecto del comprobante fiscal para el contribuyente emisor: ingreso, egreso, traslado, nota de credito, pago; Representados por los valores: (I|E|T|N|P)
    @Pattern(regexp = "I|E|T|N|P")
    private String CfdiType;

    //Forma de pago (Efectivo, Tarjeta, Por definir, etc), algún valor válido del catálogo
    @Pattern(regexp = "01|02|03|04|05|06|08|12|13|14|15|17|23|24|25|26|27|28|29|30|31|99")
    private String PaymentForm;

    //Método de pago (PUE = Una sola exhibición, PPD = Parcialidades)
    @Pattern(regexp = "PUE|PPD")
    private String PaymentMethod;

    //La moneda puede ser cualquiera del catálogo, por ejemplo el peso mexicano es MXN
    @Pattern(regexp = "MXN|USD")
    private String Currency;

    @Pattern(regexp = "^([-+]?\\d{4}(?!\\d{2}\\b))((-?)(0[1-9]|1[0-2])(\\3([12]\\d|0[1-9]|3[01]))?|W([0-4]\\d|5[0-2])(-?[1-7])?|(00[1-9]|0[1-9]\\d|[12]\\d{2}|3([0-5]\\d|6[1-6])))([T\\s]((([01]\\d|2[0-3])((:?)[0-5]\\d)?|24\\:?00)([\\.,]\\d+(?!:))?)?(\\17[0-5]\\d([\\.,]\\d+)?)?([zZ]|([-+])([01]\\d|2[0-3]):?([0-5]\\d)?)?)?)?$")
    private String Date;

    //Lugar de expedición, es representado por un Código Postal, que pertenece a una sucursal
    @Pattern(regexp = "[0-9]{5}")
    private String ExpeditionPlace;

    //Opcional, se puede especificar el Folio (atributo para control interno).
    @Pattern(regexp = "[^|]{1,40}")
    @Size(min = 1, max = 40)
    private String Folio;

    //Es a quien va dirigido el CFDI. En la estructura es representado por un nodo que se coloca en el atributo Receiver
    private Receiver Receiver;

    //Conjunto de productos y servicioS que cubre el comprobante
    private List<Item> Items;

    @Pattern(regexp = "[a-zA-Z0-9]{1,10}")
    @Size(min = 0, max = 10)
    private String Serie;
    private String CertNumber;
    private String PaymentTerms;
    @Pattern(regexp = "[^|]{1,100}")
    private String PaymentConditions;
    @Pattern(regexp = "^\\d{1,4}?$")
    @Size(min = 4, max = 4)
    private String PaymentAccountNumber;
    @Pattern(regexp = "[0-9]{1,18}(.[0-9]{1,6})?")
    @DecimalMin(value = "1E-06")
    @DecimalMax(value = "1.79769313486232E+308")
    private Double ExchangeRate;
    @Size(min = 3, max = 3)
    private String Status;
    private String OriginalString;
    @Size(max = 100)
    private String OrderNumber;
    @Size(max = 50)
    private String PaymentBankName;
    private String Observations;
    private GlobalInformation GlobalInformation;

}




