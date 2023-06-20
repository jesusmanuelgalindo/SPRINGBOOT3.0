package gob.jmas.service.pago;

import gob.jmas.dto.ConceptoDePagoDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.model.comercial.Pago;
import gob.jmas.model.facturacion.Factura;
import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.repository.comercial.PagoRepository;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.service.formaDePago.FormaDePagoService;
import gob.jmas.utils.Censurar;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagoServiceImpl implements  PagoService{
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private FormaDePagoService formaDePagoService;

    @Autowired
    FacturaService facturaService;

    @Autowired
    Censurar censurar;
    @Override
    public PagoDto  detalleDePago(String cuenta, Integer caja, Integer referencia ) throws Excepcion {

            //Se trae los registros de la base de datos
            List<Pago> pagos = pagoRepository.consultarDetalleDePago(cuenta, caja, referencia);

            if (pagos.size() == 0)
                return null;

            //Verifica si no ha sido facturado con anterioridad
            Factura facturaLocalizada = facturaService.findFacturaByCuentaCajaReferencia(cuenta, caja, referencia);
            if (facturaLocalizada != null)
                throw new Excepcion(HttpStatus.ALREADY_REPORTED, "El Ticket #" + referencia + " ya fue facturado con anterioridad y la factura fue enviada a: " + censurar.censurarEmail(facturaLocalizada.getEmailRegistrado()) + (facturaLocalizada.getEmailAdicional().length() > 0 ? " y a " + censurar.censurarEmail(facturaLocalizada.getEmailAdicional()) : ""));

            //Verifica que la fecha del ticket sea del mes actual
            if (pagos.get(0).getFechaDePago().getMonthValue() != LocalDate.now().getMonthValue() || pagos.get(0).getFechaDePago().getYear() != LocalDate.now().getYear())
                throw new Excepcion(HttpStatus.FORBIDDEN, "El ticket de pago corresponde a un mes anterior");


            //Prepara los datos que va devolver
            PagoDto pagoDto = new PagoDto();
            pagoDto.setCuenta(pagos.get(0).getCuenta());
            pagoDto.setNombre(pagos.get(0).getNombre());
            pagoDto.setDireccion(pagos.get(0).getDireccion());
            pagoDto.setCaja(pagos.get(0).getCaja());
            pagoDto.setReferencia(pagos.get(0).getReferencia());

            FormaDePago formaDePago = formaDePagoService.getFormaDePagoByClaveComercial(pagos.get(0).getFormaDePago());
            if (formaDePago == null)
                throw new Excepcion(HttpStatus.NOT_FOUND, "La forma de Pago '" + pagos.get(0).getFormaDePago() + "' registrada en el Ticket,no existe en el Catálogo del SAT. Intente nuevamente. Si el problema persiste reportelo a JMAS Parral");
            else
                pagoDto.setFormaDePago(formaDePago);


            pagoDto.setFechaDePago(pagos.get(0).getFechaDePago());

            List<ConceptoDePagoDto> conceptosDePagoDto = new ArrayList<>();

            for (Pago pago : pagos) {
                ConceptoDePagoDto conceptoDePagoDto = new ConceptoDePagoDto();
                conceptoDePagoDto.setClaveComercial(pago.getClaveComercial());
                conceptoDePagoDto.setClaveSat(pago.getClaveSat());
                conceptoDePagoDto.setDescripcion(pago.getDescripcion());
                conceptoDePagoDto.setMonto(pago.getMonto());
                conceptoDePagoDto.setTasa(pago.getTasa());
                conceptosDePagoDto.add(conceptoDePagoDto);
            }
            pagoDto.setConceptos(conceptosDePagoDto);
            return pagoDto;
        }


}
