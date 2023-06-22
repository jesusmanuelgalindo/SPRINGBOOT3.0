package gob.jmas.dto.facturamaFactura;

import gob.jmas.model.facturacion.ConceptoDePago;
import gob.jmas.model.facturacion.Factura;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FacturamaFactura {

    private String Serie;
    private String Currency;
    private String ExpeditionPlace;
    private String PaymentConditions;
    private String Folio;
    private String CfdiType;
    private String PaymentForm;
    private String PaymentMethod;
    private Receiver Receiver;
    private List<Item> Items;

    // Constructor, getters y setters


}




