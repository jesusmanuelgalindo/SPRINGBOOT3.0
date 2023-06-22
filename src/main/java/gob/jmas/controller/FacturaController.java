package gob.jmas.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import gob.jmas.dto.FacturaDto;
import gob.jmas.dto.NuevaFacturaDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.dto.ReceptorDto;
import gob.jmas.dto.facturamaFactura.FacturamaFactura;
import gob.jmas.dto.facturamaResponse.FacturamaResponse;
import gob.jmas.model.facturacion.*;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.service.facturama.FacturamaService;
import gob.jmas.service.pago.PagoService;
import gob.jmas.service.usoDeCfdi.UsoDeCfdiService;
import gob.jmas.utils.Censurar;
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

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @PostMapping("/nueva")
    @ApiOperation(value = "Registra una nueva Factura en la base de datos")
    public Mono<ResponseEntity<Respuesta<FacturamaResponse>>> crearFactura(@Valid @RequestBody NuevaFacturaDto nuevaFacturaDto) {
        String nombreDelEndpoint = request.getRequestURI();
        try {
            // Crear Factura
            Factura nueva = facturaService.createFactura(nuevaFacturaDto);
            FacturamaFactura enviar= convertir.facturaAFacturamaFactura(nueva);
            System.out.println(convertir.objetoAJsonString(enviar));

            // Realizar petición a Facturama y suscribirse a la respuesta
            return facturamaService.enviarFactura(enviar)
                    .map(facturamaResponse -> ResponseEntity.ok(new Respuesta<>(facturamaResponse, 1, "", nombreDelEndpoint)))
                    .onErrorResume(error -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new Respuesta<>(null, 0, error.getMessage(), nombreDelEndpoint))))
                    .subscribeOn(Schedulers.boundedElastic());
        } catch (Excepcion e) {
            // Cualquier otra excepción se regresa en la respuesta
            return Mono.just(ResponseEntity.status(e.getTipo())
                    .body(new Respuesta<>(null, 0, e.getMessage(), nombreDelEndpoint)));
        }
    }

}
