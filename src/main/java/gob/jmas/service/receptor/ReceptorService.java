package gob.jmas.service.receptor;

import gob.jmas.model.facturacion.Receptor;
import gob.jmas.utils.Excepcion;

import java.util.List;

public interface ReceptorService
{
    Receptor getReceptorById(Integer id) throws Excepcion;
    Receptor getReceptorByRfc(String rfc) throws Excepcion;
    List<Receptor> getAllReceptores();
    Receptor createReceptor(Receptor receptorNuevo);
    Receptor updateReceptor(Integer id, Receptor receptorActualizado);
    void deleteReceptor(Integer id);
}
