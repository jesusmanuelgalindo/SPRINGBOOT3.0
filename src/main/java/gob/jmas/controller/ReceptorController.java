package gob.jmas.controller;

import gob.jmas.dto.ReceptorDto;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.service.receptor.ReceptorService;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;

@RestController
@Validated
@RequestMapping("/receptores")
public class ReceptorController {

    @Autowired
    ReceptorService receptorService;
//    //
    @GetMapping("/{rfc}")
    @ApiOperation(value = "Obtiene los datos del Receptor buscandolo por RFC")
    public Respuesta<ReceptorDto> getReceptorByRFC(@PathVariable   @Pattern(regexp = "^[A-Za-z]{3,4}\\d{6}[A-Za-z0-9]{2}\\d$", message = "NO INGRESÓ UN RFC VALIDO. VERIFIQUE EL FORMATO") String rfc)
    {
        String nombreDelEndpoint="/receptores/{rfc}";
        try
        {
            Receptor receptor = receptorService.getReceptorByRfc(rfc);
            return new Respuesta<ReceptorDto>(nombreDelEndpoint,0, HttpStatus.OK,new ReceptorDto(receptor),1,"");
        }
        catch (Excepcion e)
        {
            return new Respuesta<ReceptorDto>(nombreDelEndpoint,0, e.getTipo(),null,0,e.getMessage());
        }
    }

    @PostMapping("/nuevo/")
    @ApiOperation(value = "Registra un nuevo Receptor en la base de datos")
    public Respuesta<ReceptorDto> crearReceptor(@RequestBody ReceptorDto receptorDto)
    {
        String nombreDelEndpoint="/receptores/nuevo/";
        try
        {
            Receptor receptor = receptorService.createReceptor(receptorDto);

            return new Respuesta<ReceptorDto>(nombreDelEndpoint,0, HttpStatus.OK,new ReceptorDto(receptor),1,"");
        }
        catch (Excepcion e)
        {
            return new Respuesta<ReceptorDto>(nombreDelEndpoint,0, e.getTipo(),null,0,e.getMessage());
        }
    }


    }
