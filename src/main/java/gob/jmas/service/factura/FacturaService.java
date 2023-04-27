package gob.jmas.service.factura;
import gob.jmas.model.Factura;
import java.util.List;

public interface FacturaService {
    Factura getFacturaById(Integer id);
    List<Factura> getAllFacturas();
    Factura createFactura(Factura facturaNueva);
    Factura updateFactura(Integer noFactura, Factura facturaActualizada);
    void deleteFactura(Integer noFactura);
}