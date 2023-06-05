package gob.jmas.service.pago;

import gob.jmas.dto.PagoDto;
import gob.jmas.model.comercial.Pago;

import java.util.List;

public interface PagoService {

    PagoDto   detalleDePago(String cuenta, Integer caja, Integer referencia);
}
