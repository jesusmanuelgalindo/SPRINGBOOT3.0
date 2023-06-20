package gob.jmas.controller;


import gob.jmas.dto.NuevaFacturaDto;
import gob.jmas.dto.ReceptorDto;
import gob.jmas.model.facturacion.Factura;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.utils.Censurar;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/facturas")

public class FacturaController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    FacturaService facturaService;

    @Autowired
    Censurar censurar;

    /*
    @PostMapping("/nueva")
    @ApiOperation(value = "Registra un nueva Factura en la base de datos")
    public ResponseEntity<Respuesta<Factura>> crearFactura(@Valid @RequestBody NuevaFacturaDto nuevaFacturaDto)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try {
            Factura factura= facturaService.findFacturaByCuentaCajaReferencia(nuevaFacturaDto.getCuenta(), nuevaFacturaDto.getCaja(), nuevaFacturaDto.getReferencia());
            if(factura!=null)
                  return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new Respuesta<Factura>(null,1,"El Ticket #"+nuevaFacturaDto.getReferencia()+" ya fue facturado con anterioridad y la factura fue enviada a: "+censurar.censurarEmail(facturaLocalizada.getEmailRegistrado() ) + (facturaLocalizada.getEmailAdicional().length()>0?" y a "+censurar.censurarEmail(facturaLocalizada.getEmailAdicional()) :""),nombreDelEndpoint));



        return null;



        }
        catch (Excepcion excepcion)
        {

                 return ResponseEntity.status(excepcion.getTipo()).body(new Respuesta<Factura>(null, 0, excepcion.getMessage(), nombreDelEndpoint));
        }


    }*/

}
