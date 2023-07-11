package gob.jmas.controller;


import gob.jmas.dto.NuevaFacturaDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.dto.facturamaFactura.Cfdi;
import gob.jmas.dto.facturamaResponse.FacturamaResponse;
import gob.jmas.model.facturacion.*;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.service.facturama.FacturamaService;
import gob.jmas.utils.Convertir;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/facturas")

public class FacturaController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    FacturaService facturaService;

    @Autowired
    FacturamaService facturamaService;


    @Autowired
    Convertir convertir;

//    @PostMapping("/nueva")
//    @ApiOperation(value = "Registra una nueva Factura en la base de datos")
//    public Mono<ResponseEntity<Respuesta<FacturamaResponse>>> crearFactura(@Valid @RequestBody NuevaFacturaDto nuevaFacturaDto) {
//        String nombreDelEndpoint = request.getRequestURI();
//        try {
//            // Crear Factura
//            Factura nueva = facturaService.createFactura(nuevaFacturaDto);
//            Cfdi enviar= convertir.facturaACfdi(nueva);
//            //System.out.println(convertir.objetoAJsonString(enviar));
//
//
//
//            // Realizar petición a Facturama y suscribirse a la respuesta
//            return facturamaService.enviarFactura(enviar);
//        } catch (Excepcion e) {
//            // Cualquier otra excepción se regresa en la respuesta
//            return Mono.just(ResponseEntity.status(e.getTipo())
//                    .body(new Respuesta<>(null, 0, e.getMessage(), nombreDelEndpoint)));
//        }
//    }

    @PostMapping("/nueva")
    @ApiOperation(value = "Registra una nueva Factura en la base de datos")
    public Mono
            <ResponseEntity<Respuesta<Map<String, Object>>>> crearFactura(@Valid @RequestBody NuevaFacturaDto nuevaFacturaDto) {
        String nombreDelEndpoint = request.getRequestURI();
        try {
            // Crear Factura
            Factura nueva = facturaService.createFactura(nuevaFacturaDto);
            Cfdi enviar = convertir.facturaACfdi(nueva);
        //System.out.println(convertir.objetoAJsonString(enviar));


        // Realizar petición a Facturama y suscribirse a la respuesta
            return facturamaService.enviarFactura(enviar)
                    .map(facturamaResponse -> ResponseEntity.ok(new Respuesta<>(facturamaResponse, 1, "Factura creada exitosamente", nombreDelEndpoint)))
                    .defaultIfEmpty(ResponseEntity.notFound().build());
      //
    }
        catch (Excepcion e)
        {
            // Cualquier otra excepción la regresa en la respuesta
            return Mono.error(e)
                    .map(exception -> ResponseEntity.status(e.getTipo()).body(new Respuesta<Map<String, Object>>(null, 0, e.getMessage(), nombreDelEndpoint)));
        }
    }
}
