package gob.jmas.controller;

import gob.jmas.model.Factura;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/factura")
@Api(value = "users", description = "Operaciones relacionadas con las facturas")

public class FacturaController {

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtiene una factura por su ID")
    public ResponseEntity<Factura> getUserById(@PathVariable Long id) {
        //Factura factura = userService.getUserById(id);
        return ResponseEntity.ok(null);
    }
}
