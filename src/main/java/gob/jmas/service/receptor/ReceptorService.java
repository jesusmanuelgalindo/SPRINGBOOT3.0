package gob.jmas.service.receptor;

import gob.jmas.model.Receptor;
import java.util.List;

public interface ReceptorService
{
    Receptor getReceptorById(Integer id);
    Receptor getReceptorByRfc(String rfc);
    List<Receptor> getAllReceptores();
    Receptor createReceptor(Receptor receptorNuevo);
    Receptor updateReceptor(Integer id, Receptor receptorActualizado);
    void deleteReceptor(Integer id);
}
