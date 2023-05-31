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
        if (optionalReceptor.isPresent()) {
            return optionalReceptor.get();
        }
        else {
            throw new Excepcion(HttpStatus.NO_CONTENT,"NO EXISTE NINGUN REGISTRO EN LA BASE DE DATOS DE RECEPTORES QUE COINCIDA CON EL ID '"+id.toString()+"'");
        }
    }

    @Override
    public Receptor getReceptorByRfc(String rfc) throws Excepcion
    {
        Optional<Receptor> optionalReceptor = receptorRepository.findByRfc(rfc);
        if (optionalReceptor.isPresent()) {
            return optionalReceptor.get();
        }
        else {
            throw new Excepcion(HttpStatus.NOT_FOUND,"NO EXISTE NINGUN REGISTRO EN LA BASE DE DATOS DE RECEPTORES QUE COINCIDA CON EL RFC '"+rfc+"'");
        }
    }


    @Override
    public Receptor createReceptor(Receptor receptor) throws Excepcion {
        Optional<Receptor> optionalReceptor = receptorRepository.findByRfc(receptor.getRfc());
        if (optionalReceptor.isPresent()) {
            throw new Excepcion(HttpStatus.CONFLICT,"EL RFC '"+receptor.getRfc()+"' YA FUÉ REGISTRADO ANTERIORMENTE EN LA BASE DE DATOS");
        }
        else
        {
            Receptor receptorNuevo = new Receptor();
            receptorNuevo.setRfc(receptor.getRfc());
            receptorNuevo.setRazonSocial(receptor.getRazonSocial());
            receptorNuevo.setCodigoPostal(receptor.getCodigoPostal());
            receptorNuevo.setEmail(receptor.getEmail());
            receptorNuevo.setRegimenFiscal(regimenFiscalService.getRegimenFiscalById(receptor.getRegimenFiscal().getId()));
            return receptorRepository.save(receptorNuevo);
        }
    }



    @Override
    public Receptor updateReceptor(Integer id, Receptor receptor) throws Excepcion {
        // Buscamos la receptor a actualizar
        Optional<Receptor> optionalReceptor = receptorRepository.findById(receptor.getId());
        if (optionalReceptor.isPresent())
        {
            Receptor receptorExistente = optionalReceptor.get();

            // Verificar los campos que no están vacíos en el receptor recibido y actualizar el receptor existente
            if (receptor.getRfc() != null && !receptor.getRfc().isEmpty()) {
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
                receptorExistente.setRegimenFiscal(regimenFiscal);
            }

            // Guardamos los cambios en la base de datos y retornamos la receptor actualizada
            return receptorRepository.save(receptorExistente);
        }
        else
        {
            throw new Excepcion(HttpStatus.NOT_FOUND,"NO SE REALIZO ACTUALIZACION DEBIDO A QUE NO EXISTE UN RECEPTOR CON EL ID '"+receptor.getId()+"' EN LA BASE DE DATOS");
        }

    }
    @Override
    public List<Receptor> getAllReceptores() {
        return receptorRepository.findAll();
    }

    @Override
    public void deleteReceptor(Integer id) {
        Optional<Receptor> optionalReceptor = receptorRepository.findById(id);
        if (optionalReceptor.isPresent()) {
            receptorRepository.delete(optionalReceptor.get());
        } else {
            throw new RuntimeException("receptor no encontrado");
        }
    }
}
