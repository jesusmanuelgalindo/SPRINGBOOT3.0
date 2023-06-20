package gob.jmas.service.formaDePago;

import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.repository.facturacion.FormaDePagoRepository;
import gob.jmas.utils.Excepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FormaDePagoServiceImpl implements FormaDePagoService{

    @Autowired
    FormaDePagoRepository formaDePagoRepository;
    public FormaDePago getFormaDePagoByClaveComercial(String claveComercial) throws Excepcion
    {
        Optional<FormaDePago> optionalFormaDePago = formaDePagoRepository.findByClaveComercial(claveComercial);
        return optionalFormaDePago.orElse(null);
    }
}
