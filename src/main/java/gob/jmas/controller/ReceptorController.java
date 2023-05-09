package gob.jmas.controller;

import gob.jmas.model.facturacion.Receptor;
import gob.jmas.service.receptor.ReceptorService;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.intellij.lang.annotations.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/receptores")
public class ReceptorController {

    @Autowired
    ReceptorService receptorService;
//    //"^([A-ZÑ&]{3,4})(\\d{2})(0[1-9]|1[0-2])([A-Z\\d]{2})(\\d{1}|[A-Z])([A-V\\d]{1})([0-9A-Za-z]{2}|[A-Za-z0-9]{3})$"
    @GetMapping("/{rfc}")
    @ApiOperation(value = "Obtiene los datos del Receptor buscandolo por RFC")
    public Respuesta<Receptor> getReceptorByRFC(@PathVariable  String rfc)
    {
        try
        {
            Receptor receptor = receptorService.getReceptorByRfc(rfc);
            return new Respuesta<Receptor>("/receptores/{rfc}",0, HttpStatus.OK,receptor,1,"");
        }
        catch (Excepcion e)
        {
            return new Respuesta<Receptor>("/receptores/{rfc}",0, e.getTipo(),null,0,e.getMessage());
        }
    }
}
