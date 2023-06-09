package gob.jmas.controller;

import gob.jmas.dto.ReceptorDto;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.service.receptor.ReceptorService;
import gob.jmas.service.regimenFiscal.RegimenFiscalService;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@Validated
@RequestMapping("/regimenFiscal")
public class RegimenFiscalController {

    @Autowired
    RegimenFiscalService regimenFiscalService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/lista")
    @ApiOperation(value = "Obtiene el listado del Catalogo de Regimenes Fiscales")
    public ResponseEntity<Respuesta<List<RegimenFiscal>>> getAllRegimenFiscal()
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            List<RegimenFiscal> regimenesFiscales = regimenFiscalService.findAllRegimenFiscal();
            return ResponseEntity.ok(new Respuesta<List<RegimenFiscal>>(regimenesFiscales, regimenesFiscales.size(),"",nombreDelEndpoint));
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<List<RegimenFiscal>>(null,0,e.getMessage(),nombreDelEndpoint));
        }
    }
}
