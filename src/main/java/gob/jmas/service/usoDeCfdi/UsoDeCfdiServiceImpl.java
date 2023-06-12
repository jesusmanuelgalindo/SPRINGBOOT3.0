package gob.jmas.service.usoDeCfdi;

import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.model.facturacion.UsoDeCfdi;
import gob.jmas.repository.facturacion.RegimenFiscalRepository;
import gob.jmas.repository.facturacion.UsoDeCfdiRepository;
import gob.jmas.service.regimenFiscal.RegimenFiscalService;
import gob.jmas.utils.Excepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsoDeCfdiServiceImpl implements UsoDeCfdiService {
    @Autowired
    private UsoDeCfdiRepository usoDeCfdiRepository;


    @Override
    public UsoDeCfdi getUsoDeCfdiById(Integer id) throws Excepcion
    {
        Optional<UsoDeCfdi> optionalUsoDeCfdi = usoDeCfdiRepository.findById(id);
        if (optionalUsoDeCfdi.isPresent()) {
            return optionalUsoDeCfdi.get();
        }
        else {
            throw new Excepcion(HttpStatus.NOT_FOUND,"No existe ningun registro en la base de datos de usos de CFDI que coincida con el id '"+id.toString()+"'");
        }
    }

    @Override
    public List<UsoDeCfdi> findAllUsoDeCfdi() throws Excepcion
    {
        try
        {
            return usoDeCfdiRepository.findAll();
        }
        catch (Exception ex)
        {
            throw new Excepcion(HttpStatus.INTERNAL_SERVER_ERROR,"Error al obtener usos de CFDI:'"+ex.getMessage()+"'");
        }
    }

}

