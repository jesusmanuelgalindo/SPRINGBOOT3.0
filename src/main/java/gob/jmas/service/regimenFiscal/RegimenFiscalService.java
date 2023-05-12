package gob.jmas.service.regimenFiscal;

import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.utils.Excepcion;

public interface RegimenFiscalService {

    RegimenFiscal getRegimenFiscalById(Integer id) throws Excepcion;
}
