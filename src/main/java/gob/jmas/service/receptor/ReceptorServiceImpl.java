package gob.jmas.service.receptor;

import gob.jmas.dto.ReceptorDto;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.repository.facturacion.ReceptorRepository;
import gob.jmas.service.regimenFiscal.RegimenFiscalService;
import gob.jmas.utils.Excepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceptorServiceImpl implements  ReceptorService {


    @Autowired
    private ReceptorRepository receptorRepository;

    @Autowired
    private RegimenFiscalService regimenFiscalService;

    @Override
    public Receptor getReceptorById(Integer id) throws Excepcion
    {
        Optional<Receptor> optionalReceptor = receptorRepository.findById(id);
        return optionalReceptor.orElse(null);
    }

    @Override
    public Receptor getReceptorByRfc(String rfc) throws Excepcion
    {
        Optional<Receptor> optionalReceptor = receptorRepository.findByRfc(rfc);
        return optionalReceptor.orElse(null);
    }


    @Override
    public Receptor createReceptor(Receptor receptor) throws Excepcion {
        Optional<Receptor> optionalReceptor = receptorRepository.findByRfc(receptor.getRfc());
        if (optionalReceptor.isPresent())
            throw new Excepcion(HttpStatus.CONFLICT, "El RFC '" + receptor.getRfc() + "' Ya fué registrado anteriormente en la base de datos");

        Receptor receptorNuevo = new Receptor();
        receptorNuevo.setRfc(receptor.getRfc());
        receptorNuevo.setRazonSocial(receptor.getRazonSocial());
        receptorNuevo.setCodigoPostal(receptor.getCodigoPostal());
        receptorNuevo.setEmail(receptor.getEmail());

        RegimenFiscal regimenFiscal = regimenFiscalService.getRegimenFiscalById(receptor.getRegimenFiscal().getId());
        if (regimenFiscal == null)
            throw new Excepcion(HttpStatus.NOT_FOUND, "No existe ningun registro en la base de datos de regimenes fiscales que coincida con el id '" + receptor.getRegimenFiscal().getId().toString() + "'");

        receptorNuevo.setRegimenFiscal(regimenFiscal);
        return receptorRepository.save(receptorNuevo);
    }


    @Override
    public Receptor updateReceptor(Integer id, Receptor receptor) throws Excepcion {
        // Buscamos la receptor a actualizar
        Receptor receptorExistente = getReceptorById(id);
        if (receptorExistente == null)
            throw new Excepcion(HttpStatus.NOT_FOUND, "No existe ningun registro en la base de datos de Receptores que coincida con el id '" + id.toString() + "'");

        // Verificar los campos que no están vacíos en el receptor recibido y actualizar el receptor existente
        if (receptor.getRfc() != null && !receptor.getRfc().isEmpty()) {

           Receptor verificarRfc= getReceptorByRfc(receptor.getRfc());
           if(verificarRfc!=null && !verificarRfc.getId().equals(id))
               throw new Excepcion(HttpStatus.CONFLICT, "El RFC '" + receptor.getRfc() + "' Ya se encuentra en uso en un registro previo.");

            receptorExistente.setRfc(receptor.getRfc());


        }
        if (receptor.getRazonSocial() != null && !receptor.getRazonSocial().isEmpty()) {
            receptorExistente.setRazonSocial(receptor.getRazonSocial());
        }
        if (receptor.getCodigoPostal() != null) {
            receptorExistente.setCodigoPostal(receptor.getCodigoPostal());
        }
        if (receptor.getEmail() != null && !receptor.getEmail().isEmpty()) {
            receptorExistente.setEmail(receptor.getEmail());
        }

        if (receptor.getRegimenFiscal() != null && receptor.getRegimenFiscal().getId() != null) {

            RegimenFiscal regimenFiscal = regimenFiscalService.getRegimenFiscalById(receptor.getRegimenFiscal().getId());
            if (regimenFiscal == null)
                throw new Excepcion(HttpStatus.NOT_FOUND, "No existe ningun registro en la base de datos de regimenes fiscales que coincida con el id '" + receptor.getRegimenFiscal().getId().toString() + "'");

            receptorExistente.setRegimenFiscal(regimenFiscal);
        }
        // Guardamos los cambios en la base de datos y retornamos la receptor actualizada
        return receptorRepository.save(receptorExistente);
    }
    @Override
    public List<Receptor> getAllReceptores() {
        return receptorRepository.findAll();
    }

    @Override
    public void deleteReceptor(Integer id) {
        Receptor receptorExistente = getReceptorById(id);
        if(receptorExistente==null)
            throw new Excepcion(HttpStatus.NOT_FOUND,"No existe ningun registro en la base de datos de Receptores que coincida con el id '"+id.toString()+"'");

        receptorRepository.delete(receptorExistente);
    }
}
