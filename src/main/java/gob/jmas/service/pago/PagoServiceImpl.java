package gob.jmas.service.pago;

import gob.jmas.dto.ConceptoDePagoDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.model.comercial.Pago;
import gob.jmas.repository.comercial.PagoRepository;
import gob.jmas.utils.Excepcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PagoServiceImpl implements  PagoService{
    @Autowired
    private PagoRepository pagoRepository;
    @Override
    public PagoDto  detalleDePago(String cuenta, Integer caja, Integer referencia) throws Excepcion
    {
        List<Pago> pagos=pagoRepository.consultarDetalleDePago(cuenta,caja,referencia);

        if(pagos.size()>0) {
            PagoDto pagoDto = new PagoDto();
            pagoDto.setCuenta(pagos.get(0).getCuenta());
            pagoDto.setNombre(pagos.get(0).getNombre());
            pagoDto.setDireccion(pagos.get(0).getDireccion());
            pagoDto.setCaja(pagos.get(0).getCaja());
            pagoDto.setReferencia(pagos.get(0).getReferencia());
            pagoDto.setFormaDePago(pagos.get(0).getFormaDePago());
            pagoDto.setFechaDePago(pagos.get(0).getFechaDePago());

            List<ConceptoDePagoDto> conceptosDePagoDto = new ArrayList<>();

            for (Pago pago : pagos) {
                ConceptoDePagoDto conceptoDePagoDto = new ConceptoDePagoDto();
                conceptoDePagoDto.setClave(pago.getClave());
                conceptoDePagoDto.setConceptoFiscal(pago.getConceptoFiscal());
                conceptoDePagoDto.setConcepto(pago.getConcepto());
                conceptoDePagoDto.setMonto(pago.getMonto());
                conceptoDePagoDto.setTasa(pago.getTasa());
                conceptosDePagoDto.add(conceptoDePagoDto);
            }
            pagoDto.setConceptos(conceptosDePagoDto);

            return pagoDto;
        }
        else  {
        throw new Excepcion(HttpStatus.NOT_FOUND,"NO EXISTE NINGUN REGISTRO DE PAGO EN LA BASE DE DATOS QUE COINCIDA CON LOS DATOS INGRESADOS (CAJA:'"+caja.toString()+"',REFERENCIA:'"+referencia.toString()+"',CUENTA:'"+cuenta+"')");
    }

    }


}
