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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@Validated
@RequestMapping("/regimenFiscal")
public class RegimenFiscalController {

    @Autowired
    RegimenFiscalService regimenFiscalService;
    @GetMapping("/lista")
    @ApiOperation(value = "Obtiene el listado del Catalogo de Regimenes Fiscales")
    public Respuesta<List<RegimenFiscal>> getAllRegimenFiscal()
    {
        String nombreDelEndpoint="/regimenFiscal/lista";
        try
        {
            List<RegimenFiscal> regimenesFiscales = regimenFiscalService.findAllRegimenFiscal();
            return new Respuesta<List<RegimenFiscal>>(nombreDelEndpoint,0, HttpStatus.OK,regimenesFiscales, regimenesFiscales.size(),"");
        }
        catch (Excepcion e)
        {
            return new Respuesta<List<RegimenFiscal>>(nombreDelEndpoint,0, e.getTipo(),null,0,e.getMessage());
        }
    }
}
