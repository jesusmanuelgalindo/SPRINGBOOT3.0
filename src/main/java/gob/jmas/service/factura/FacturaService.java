package gob.jmas.service.factura;
import gob.jmas.model.facturacion.Factura;
import java.util.List;

public interface FacturaService {
    Factura findFacturaByCuentaCajaReferencia(String cuenta, Integer caja, Integer referencia);
    Factura getFacturaById(Integer id);

    List<Factura> getAllFacturas();
    Factura createFactura(Factura facturaNueva);
    Factura updateFactura(Integer id, Factura facturaActualizada);
    void deleteFactura(Integer id);
    void EnviarFactura(Factura factura);
}