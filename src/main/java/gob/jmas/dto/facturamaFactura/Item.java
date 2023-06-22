package gob.jmas.dto.facturamaFactura;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Item {
    private String ProductCode;
    private String IdentificationNumber;
    private String Description;
    private String Unit;
    private String UnitCode;
    private double UnitPrice;
    private double Quantity;
    private double Subtotal;
    private double Discount;
    private List<Tax> Taxes;
    private double Total;

    // Constructor, getters y setters



}