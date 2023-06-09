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

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/pago")
public class PagoController {

    @Autowired
    PagoService pagoService;

    @PostMapping("/buscar")
    @ApiOperation(value = "Localiza un ticket de pago en la base de datos")
    public ResponseEntity<Respuesta<PagoDto>> buscarPago(@RequestBody PagoBuscarDto pagoBuscarDto)
    {
        String nombreDelEndpoint="/pago/buscar/";
        try
        {
            PagoDto  pagoDto= pagoService.detalleDePago(pagoBuscarDto.getCuenta(), pagoBuscarDto.getCaja(), pagoBuscarDto.getReferencia());

            if(pagoDto.getFechaDePago().getMonthValue() == LocalDate.now().getMonthValue() && pagoDto.getFechaDePago().getYear() == LocalDate.now().getYear())
                return ResponseEntity.ok(new Respuesta<PagoDto >(pagoDto,1,""));
            else
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new Respuesta<PagoDto>  (null,0,"EL TICKET DE PAGO CORRESPONDE A UN MES ANTERIOR"));
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<PagoDto>  (null,0,e.getMessage()));
        }
    }

}
