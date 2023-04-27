package gob.jmas.service.factura;

import gob.jmas.model.Factura;
import gob.jmas.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Override
    public Factura getFacturaById(Integer id) {
        Optional<Factura> optionalFactura = facturaRepository.findById(id);
        if (optionalFactura.isPresent()) {
            return optionalFactura.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Override
    public List<Factura> getAllFacturas() {
        return facturaRepository.findAll();
    }

    @Override
    public Factura createFactura(Factura facturaNueva) {
        return facturaRepository.save(facturaNueva);
    }

    @Override
    public Factura updateFactura(Integer id, Factura facturaActualizada) {
        // Buscamos la factura a actualizar
        Optional<Factura> optionalFactura = facturaRepository.findById(id);

        if (optionalFactura.isPresent()) {
            Factura facturaExistente = optionalFactura.get();

            // Actualizamos los campos de la factura existente con los valores de la factura actualizada
            facturaExistente.setNoFactura(facturaActualizada.getNoFactura());
            facturaExistente.setUuid(facturaActualizada.getUuid());
            facturaExistente.setFechaFacturacion(facturaActualizada.getFechaFacturacion());
            facturaExistente.setUsoDeCfdi(facturaActualizada.getUsoDeCfdi());
            facturaExistente.setReceptor(facturaActualizada.getReceptor());
            facturaExistente.setXml(facturaActualizada.getXml());
            facturaExistente.setCuenta(facturaActualizada.getCuenta());
            facturaExistente.setNombre(facturaActualizada.getNombre());
            facturaExistente.setCaja(facturaActualizada.getCaja());
            facturaExistente.setReferencia(facturaActualizada.getReferencia());
            facturaExistente.setMonto(facturaActualizada.getMonto());
            facturaExistente.setFormaDePago(facturaActualizada.getFormaDePago());
            facturaExistente.setFechaDePago(facturaActualizada.getFechaDePago());
            facturaExistente.setEmailRegistrado(facturaActualizada.getEmailRegistrado());
            facturaExistente.setEmailAdicional(facturaActualizada.getEmailAdicional());
            facturaExistente.setConceptos(facturaActualizada.getConceptos());

            // Guardamos los cambios en la base de datos y retornamos la factura actualizada
            return facturaRepository.save(facturaExistente);
        }
        else
        {
            throw new RuntimeException("Factura no Encontrada");
           // throw new NotFoundException("No se encontró la factura con el número y UUID especificados");
        }
    }

    @Override
    public void deleteFactura(Integer id) {
        Optional<Factura> optionalFactura = facturaRepository.findById(id);
        if (optionalFactura.isPresent()) {
            facturaRepository.delete(optionalFactura.get());
        } else {
            throw new RuntimeException("Factura no encontrada");
        }
    }
}