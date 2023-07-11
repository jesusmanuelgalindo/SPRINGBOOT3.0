package gob.jmas.dto.facturamaFactura;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Item {
    //Atributo Opcional para relacionar el Identificador unico del producto
    private String IdProduct;

    //Clave del producto o servicio segun las claves del catalogo
    @Pattern(regexp = "[0-9]{8}")
    @Size(min = 0, max = 8)
    private String ProductCode;

    //Atributo opcional para expresar el número de serie del producto
    @Size(min = 0, max = 50)
    private String IdentificationNumber;

    //Descripción del concepto
    @Size(min = 1, max = 1000)
    private String Description;

    //Atributo opcional para especificar el nombre de la unidad
    private String Unit;

    //Atributo requerido para precisar la unidad de medida aplicable al producto
    @Size(min = 1)
    private String UnitCode;

    //Atributo requerido para precisar el valor o precio unitario del bien o servicio cubierto por el presente concepto.
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private BigDecimal UnitPrice;

    //Cantidad de productos ó servicios a comerciar
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = Integer.MAX_VALUE, fraction = 6)
    private BigDecimal Quantity;
    @Size(min = 1)

    //
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private BigDecimal Subtotal;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = Integer.MAX_VALUE, fraction = 6)
    private BigDecimal Discount;


    //Se debe registrar la clave correspondiente para indicar si la operación comercial es objeto o no de impuesto.
    //01 - No objeto de impuesto
    //02 - (Sí objeto de impuesto), se deben desglosar los Impuestos a nivel de Concepto.
    //03 - (Sí objeto del impuesto y no obligado al desglose) no se desglosan impuestos a nivel Concepto.
    //04 - (Sí Objeto de impuesto y no causa impuesto)
    @Pattern(regexp = "01|02|03|04")
    private String TaxObject;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Tax> Taxes;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
    private BigDecimal Total;


//    @DecimalMin(value = "0.0", inclusive = false)
//    @Digits(integer = Integer.MAX_VALUE, fraction = 2)
//    private Double UnitValue;

//    @Size(min = 1, max = 20)
//    private String unit;


}