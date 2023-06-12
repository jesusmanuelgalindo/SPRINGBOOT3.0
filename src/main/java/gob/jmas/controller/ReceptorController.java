package gob.jmas.controller;

import gob.jmas.dto.ReceptorDto;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.service.receptor.ReceptorService;
import gob.jmas.service.regimenFiscal.RegimenFiscalService;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.ServletWebRequest;

@RestController
@Validated
@CrossOrigin
@RequestMapping("/receptor")
public class ReceptorController {

    @Autowired
    ReceptorService receptorService;

    @Autowired
    RegimenFiscalService regimenFiscalService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/{rfc}")
    @ApiOperation(value = "Obtiene los datos del Receptor buscandolo por RFC")
    public ResponseEntity<Respuesta<ReceptorDto>> getReceptorByRFC(@PathVariable   @Pattern(regexp = "^[A-Za-z]{3,4}\\d{6}[A-Za-z0-9]{2}\\d$", message = "No ingreso un RFC valido. Verifique el formato") String rfc)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            Receptor receptor = receptorService.getReceptorByRfc(rfc);
            return ResponseEntity.ok(new Respuesta<ReceptorDto>(new ReceptorDto(receptor),1,"",nombreDelEndpoint));
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<ReceptorDto>(null,0,e.getMessage(),nombreDelEndpoint));
        }
    }

    @PostMapping("/nuevo")
    @ApiOperation(value = "Registra un nuevo Receptor en la base de datos")
    public ResponseEntity<Respuesta<ReceptorDto>> crearReceptor(@Valid @RequestBody ReceptorDto receptorDto)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            Receptor receptor= new Receptor();
            receptor.setRfc(receptorDto.getRfc());
            receptor.setRazonSocial(receptorDto.getRazonSocial());
            receptor.setCodigoPostal(receptorDto.getCodigoPostal());
            receptor.setRegimenFiscal(new RegimenFiscal(receptorDto.getIdRegimenFiscal()));
            receptor.setEmail(receptorDto.getEmail());
            receptor = receptorService.createReceptor(receptor);
            return ResponseEntity.ok(new Respuesta<ReceptorDto>(new ReceptorDto(receptor),1,"",nombreDelEndpoint));
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<ReceptorDto>(null,0,e.getMessage(),nombreDelEndpoint));
        }
    }
    @PutMapping("/actualizar/{id}")
    @ApiOperation(value = "Actualiza los datos de un Receptor en la base de datos conservando unicamente el id")
    public ResponseEntity<Respuesta<ReceptorDto>> ActualizarReceptor(@PathVariable Integer id,@Valid @RequestBody ReceptorDto receptorDto)
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            Receptor receptor= new Receptor();
            receptor.setId(id);
            receptor.setRfc(receptorDto.getRfc());
            receptor.setRazonSocial(receptorDto.getRazonSocial());
            receptor.setCodigoPostal(receptorDto.getCodigoPostal());
            receptor.setRegimenFiscal(new RegimenFiscal(receptorDto.getIdRegimenFiscal()));
            receptor.setEmail(receptorDto.getEmail());
            receptor = receptorService.updateReceptor(id,receptor);

            return ResponseEntity.ok(new Respuesta<ReceptorDto>(new ReceptorDto(receptor),1,"",nombreDelEndpoint));
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<ReceptorDto>(null,0,e.getMessage(),nombreDelEndpoint));
        }
    }

}
