package gob.jmas.controller;

import gob.jmas.model.facturacion.RegimenFiscal;
import gob.jmas.model.facturacion.UsoDeCfdi;
import gob.jmas.service.regimenFiscal.RegimenFiscalService;
import gob.jmas.service.usoDeCfdi.UsoDeCfdiService;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/usoDeCfdi")
public class UsoDeCfdiController {

    @Autowired
    UsoDeCfdiService usoDeCfdiService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/lista")
    @ApiOperation(value = "Obtiene el listado del Catalogo de Usos de CFDI")
    public ResponseEntity<Respuesta<List<UsoDeCfdi>>> getAllUsoDeCfdi()
    {
        String nombreDelEndpoint=request.getRequestURI();
        try
        {
            List<UsoDeCfdi> usoDeCfdis = usoDeCfdiService.findAllUsoDeCfdi();
            return ResponseEntity.ok(new Respuesta<List<UsoDeCfdi>>(usoDeCfdis, usoDeCfdis.size(),"",nombreDelEndpoint));
        }
        catch (Excepcion e)
        {
            return ResponseEntity.status(e.getTipo()).body(new Respuesta<List<UsoDeCfdi>>(null,0,e.getMessage(),nombreDelEndpoint));
        }
    }
}