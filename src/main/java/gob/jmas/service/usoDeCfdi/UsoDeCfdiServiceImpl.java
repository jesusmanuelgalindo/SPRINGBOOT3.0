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
        return optionalUsoDeCfdi.orElse(null);
    }

    @Override
    public List<UsoDeCfdi> findAllUsoDeCfdi() throws Excepcion
    {
        return usoDeCfdiRepository.findAll();
    }

}

