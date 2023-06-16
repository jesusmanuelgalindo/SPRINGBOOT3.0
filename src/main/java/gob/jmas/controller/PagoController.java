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
    public ResponseEntity<Respuesta<?>> buscarPago(@Valid @RequestBody PagoBuscarDto pagoBuscarDto)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            PagoDto  pagoDto= pagoService.detalleDePago(pagoBuscarDto.getCuenta(), pagoBuscarDto.getCaja(), pagoBuscarDto.getReferencia());
            try
            {
                //Verifica si no ha sido facturado con anterioridad
                Factura facturaLocalizada = facturaService.findFacturaByCuentaCajaReferencia(pagoBuscarDto.getCuenta(),pagoBuscarDto.getCaja(), pagoBuscarDto.getReferencia());
                return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(new Respuesta<Factura>(null,1,"El Ticket #"+pagoBuscarDto.getReferencia()+" ya fue facturado con anterioridad y la factura fue enviada a: "+censurar.censurarEmail(facturaLocalizada.getEmailRegistrado() ) + (facturaLocalizada.getEmailAdicional().length()>0?" y a "+censurar.censurarEmail(facturaLocalizada.getEmailAdicional()) :""),nombreDelEndpoint));
            }
            catch (Excepcion e)
            {
                //Si el pago no ha sido facturado , verifica la fecha del ticket
                if(pagoDto.getFechaDePago().getMonthValue() == LocalDate.now().getMonthValue() && pagoDto.getFechaDePago().getYear() == LocalDate.now().getYear())
                {
                    //Cuando es del mes actual lo Factura
                    return ResponseEntity.ok(new Respuesta<PagoDto>(pagoDto, 1, "", nombreDelEndpoint));
                }
                else
                {
                    //Si es de otro mes lo rechaza
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Respuesta<PagoDto>(null, 0, "El ticket de pago corresponde a un mes anterior", nombreDelEndpoint));
                }
            }
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<PagoDto>  (null,0,e.getMessage(),nombreDelEndpoint));
        }
    }

}
