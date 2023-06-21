package gob.jmas.service.pago;

import gob.jmas.dto.ConceptoDePagoDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.model.comercial.Pago;
import gob.jmas.model.facturacion.Factura;
import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.repository.comercial.PagoRepository;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.service.formaDePago.FormaDePagoService;
import gob.jmas.utils.Censurar;
import gob.jmas.utils.Excepcion;
import gob.jmas.utils.Respuesta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagoServiceImpl implements  PagoService{



}
