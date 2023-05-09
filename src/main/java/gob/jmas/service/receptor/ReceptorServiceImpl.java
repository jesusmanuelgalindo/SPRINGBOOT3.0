package gob.jmas.service.receptor;

import gob.jmas.model.facturacion.Receptor;
import gob.jmas.repository.facturacion.ReceptorRepository;
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


    @Override
    public Receptor getReceptorById(Integer id) throws Excepcion
    {
        Optional<Receptor> optionalreceptor = receptorRepository.findById(id);
        if (optionalreceptor.isPresent()) {
            return optionalreceptor.get();
        }
        else {
            throw new Excepcion(HttpStatus.NOT_FOUND,"NO EXISTE NINGUN REGISTRO EN LA BASE DE DATOS QUE COINCIDA CON EL ID INGRESADO");
        }
    }

    @Override
    public Receptor getReceptorByRfc(String rfc) throws Excepcion
    {
        Optional<Receptor> optionalreceptor = receptorRepository.findByRfc(rfc);
        if (optionalreceptor.isPresent()) {
            return optionalreceptor.get();
        }
        else {
            throw new Excepcion(HttpStatus.NOT_FOUND,"NO EXISTE NINGUN REGISTRO EN LA BASE DE DATOS QUE COINCIDA CON EL RFC INGRESADO");
        }
    }

    @Override
    public List<Receptor> getAllReceptores() {
        return receptorRepository.findAll();
    }

    @Override
    public Receptor createReceptor(Receptor receptorNuevo) {
        return receptorRepository.save(receptorNuevo);
    }

    @Override
    public Receptor updateReceptor(Integer id, Receptor receptorActualizado) {
        // Buscamos la receptor a actualizar
        Optional<Receptor> optionalReceptor = receptorRepository.findById(id);

        if (optionalReceptor.isPresent()) {
            Receptor receptorExistente = optionalReceptor.get();

            // Actualizamos los campos del receptor existente con los valores del receptor actualizado
            receptorExistente.setRfc(receptorActualizado.getRfc());
            receptorExistente.setRazonSocial(receptorActualizado.getRazonSocial());
            receptorExistente.setCodigoPostal(receptorActualizado.getCodigoPostal());
            receptorExistente.setRegimenFiscal(receptorActualizado.getRegimenFiscal());
            receptorExistente.setEmail(receptorActualizado.getEmail());

            // Guardamos los cambios en la base de datos y retornamos la receptor actualizada
            return receptorRepository.save(receptorExistente);
        }
        else
        {
            throw new RuntimeException("receptor no Encontrado");
            // throw new NotFoundException("No se encontró la receptor con el número y UUID especificados");
        }
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
