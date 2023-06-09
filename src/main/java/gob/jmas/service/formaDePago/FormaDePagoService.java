package gob.jmas.service.formaDePago;

import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.utils.Excepcion;

public interface FormaDePagoService {

    FormaDePago getFormaDePagoByClaveComercial(String claveComercial)throws Excepcion;

}
