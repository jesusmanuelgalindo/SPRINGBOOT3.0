package gob.jmas.service.receptor;

import gob.jmas.dto.ReceptorDto;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.utils.Excepcion;

import java.util.List;

public interface ReceptorService
{
    Receptor getReceptorById(Integer id) throws Excepcion;
    Receptor getReceptorByRfc(String rfc) throws Excepcion;
    List<Receptor> getAllReceptores();
    Receptor createReceptor(Receptor receptor)throws Excepcion;
    Receptor updateReceptor(Integer id,  Receptor receptorActualizado) throws Excepcion;
    void deleteReceptor(Integer id);
}
