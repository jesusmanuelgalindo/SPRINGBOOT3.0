package gob.jmas.service.usoDeCfdi;

import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.model.facturacion.UsoDeCfdi;
import gob.jmas.utils.Excepcion;

import java.util.List;

public interface UsoDeCfdiService {
    UsoDeCfdi getUsoDeCfdiById(Integer id) throws Excepcion;
    List<UsoDeCfdi> findAllUsoDeCfdi() throws Excepcion;
}
