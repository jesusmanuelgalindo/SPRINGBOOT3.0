package gob.jmas.controller;

import gob.jmas.dto.PagoBuscarDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.model.facturacion.Factura;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.service.pago.PagoService;
import gob.jmas.utils.Censurar;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    PagoService pagoService;

    @Autowired
    FacturaService facturaService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    Censurar censurar;

    @PostMapping("/buscar")
    @ApiOperation(value = "Localiza un ticket de pago en la base de datos")
    public ResponseEntity<Respuesta<PagoDto>> buscarPago(@Valid @RequestBody PagoBuscarDto pagoBuscarDto)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            PagoDto  pagoDto= pagoService.detalleDePago(pagoBuscarDto.getCuenta(), pagoBuscarDto.getCaja(), pagoBuscarDto.getReferencia());
            if(pagoDto==null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Respuesta<PagoDto>  (null,0,"No existe ningun registro de pago en la base de datos que coincida con los datos ingresados (CAJA:'"+pagoBuscarDto.getCaja().toString()+"',REFERENCIA:'"+pagoBuscarDto.getReferencia().toString()+"',CUENTA:'"+pagoBuscarDto.getCuenta()+"')",nombreDelEndpoint));
            else
                return ResponseEntity.ok(new Respuesta<PagoDto>(pagoDto, 1, "", nombreDelEndpoint));

        }
        catch (Excepcion e)
        {
            //Cualquier otra excepcion la regresa en la respuesta
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<PagoDto>  (null,0,e.getMessage(),nombreDelEndpoint));
        }
    }

}
