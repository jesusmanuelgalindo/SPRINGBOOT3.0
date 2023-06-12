package gob.jmas.controller;

import gob.jmas.dto.PagoBuscarDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.model.comercial.Pago;
import gob.jmas.service.pago.PagoService;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    PagoService pagoService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/buscar")
    @ApiOperation(value = "Localiza un ticket de pago en la base de datos")
    public ResponseEntity<Respuesta<PagoDto>> buscarPago(@RequestBody PagoBuscarDto pagoBuscarDto)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            PagoDto  pagoDto= pagoService.detalleDePago(pagoBuscarDto.getCuenta(), pagoBuscarDto.getCaja(), pagoBuscarDto.getReferencia());

            if(pagoDto.getFechaDePago().getMonthValue() == LocalDate.now().getMonthValue() && pagoDto.getFechaDePago().getYear() == LocalDate.now().getYear())
                return ResponseEntity.ok(new Respuesta<PagoDto >(pagoDto,1,"",nombreDelEndpoint));
            else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Respuesta<PagoDto>  (null,0,"El ticket de pago corresponde a un mes anterior",nombreDelEndpoint));
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<PagoDto>  (null,0,e.getMessage(),nombreDelEndpoint));
        }
    }

}
