package gob.jmas.service.regimenFiscal;

import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.repository.facturacion.ReceptorRepository;
import gob.jmas.repository.facturacion.RegimenFiscalRepository;
import gob.jmas.utils.Excepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegimenFiscalServiceImpl implements RegimenFiscalService {
    @Autowired
    private RegimenFiscalRepository regimenFiscalRepository;


    @Override
    public RegimenFiscal getRegimenFiscalById(Integer id) throws Excepcion
    {
        Optional<RegimenFiscal> optionalRegimenFiscal = regimenFiscalRepository.findById(id);
        if (optionalRegimenFiscal.isPresent()) {
            return optionalRegimenFiscal.get();
        }
        else {
            throw new Excepcion(HttpStatus.NOT_FOUND,"NO EXISTE NINGUN REGISTRO EN LA BASE DE DATOS DE REGIMENES FISCALES QUE COINCIDA CON EL ID '"+id.toString()+"'");
        }
    }

    @Override
    public List<RegimenFiscal> findAllRegimenFiscal() throws Excepcion
    {
        try
        {
            return regimenFiscalRepository.findAll();
        }
        catch (Exception ex)
        {
            throw new Excepcion(HttpStatus.INTERNAL_SERVER_ERROR,"ERROR AL OBTENER REGIMENES FISCALES:'"+ex.getMessage()+"'");
        }
    }

}
