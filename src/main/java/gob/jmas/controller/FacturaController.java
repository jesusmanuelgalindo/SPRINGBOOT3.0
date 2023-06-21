package gob.jmas.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import gob.jmas.dto.FacturaDto;
import gob.jmas.dto.NuevaFacturaDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.dto.ReceptorDto;
import gob.jmas.model.facturacion.*;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.service.pago.PagoService;
import gob.jmas.service.usoDeCfdi.UsoDeCfdiService;
import gob.jmas.utils.Censurar;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/facturas")

public class FacturaController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    FacturaService facturaService;





    @PostMapping("/nueva")
    @ApiOperation(value = "Registra un nueva Factura en la base de datos")
    public ResponseEntity<Respuesta<FacturaDto>> crearFactura(@Valid @RequestBody NuevaFacturaDto nuevaFacturaDto)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {

            //Crear Factura
            Factura nueva = facturaService.createFactura(nuevaFacturaDto);

            //Timbra la Factura




            return ResponseEntity.ok(new Respuesta<FacturaDto>(new FacturaDto(nueva), 1, "", nombreDelEndpoint));

        }
        catch (Excepcion e)
        {
            //Cualquier otra excepcion la regresa en la respuesta
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<FacturaDto>  (null,0,e.getMessage(),nombreDelEndpoint));
        }
//        catch (RuntimeException e)
//        {
//            //Cualquier otra excepcion la regresa en la respuesta
//            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new Respuesta<Factura>  (null,0,e.getMessage(),nombreDelEndpoint));
//        }


    }

}
