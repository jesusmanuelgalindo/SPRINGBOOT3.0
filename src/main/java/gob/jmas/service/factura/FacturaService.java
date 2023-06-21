package gob.jmas.service.factura;
import gob.jmas.dto.FacturaDto;
import gob.jmas.dto.NuevaFacturaDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.model.facturacion.Factura;
import java.util.List;

public interface FacturaService {
    PagoDto   detalleDePago(String cuenta, Integer caja, Integer referencia);

    Factura findFacturaByCuentaCajaReferencia(String cuenta, Integer caja, Integer referencia);
    Factura getFacturaById(Integer id);

    List<Factura> getAllFacturas();

    Factura createFactura(Factura facturaNueva);

    Factura createFactura(NuevaFacturaDto nuevaFacturaDto);
    Factura updateFactura(Integer id, Factura facturaActualizada);
    void deleteFactura(Integer id);
    void EnviarFactura(Factura factura);
}