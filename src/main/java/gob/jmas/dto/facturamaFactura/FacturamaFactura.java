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

     String Serie;
     String Currency;
     String ExpeditionPlace;
     String PaymentConditions;
     String Folio;
     String CfdiType;
     String PaymentForm;
     String PaymentMethod;
     Receiver Receiver;
     List<Item> Items;

    // Constructor, getters y setters


}




