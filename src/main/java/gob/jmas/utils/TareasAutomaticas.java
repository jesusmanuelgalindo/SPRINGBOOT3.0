package gob.jmas.utils;

import gob.jmas.model.facturacion.Factura;
import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.service.factura.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;

@Component
public class TareasAutomaticas {

    @Autowired
    FacturaService facturaService;

    @Autowired
    Convertir convertir;

    private static final Logger logger = LoggerFactory.getLogger(TareasAutomaticas.class);

    @Scheduled(fixedRate = 3600000) //Cada Hora
    public void CadaHora()
    {
        logger.info("Tarea Automatica INICIADA");

        Factura ejemplo= new Factura();
        ejemplo.setNoFactura(999);
        ejemplo.setEmailRegistrado("jesusmanuelgalindo@gmail.com");
        ejemplo.setEmailAdicional("sistemasjmasparral@gmail.com");
        ejemplo.setUuid("123456789012345678901234567890123456");
        ejemplo.setFormaDePago(new FormaDePago());
        ejemplo.getFormaDePago().setDescripcion("Efectivo");
        ejemplo.setFechaDePago(LocalDate.of(2023, Month.APRIL, 28) );
      // facturaService.EnviarFactura(ejemplo);



    }

    @Scheduled(cron = "0 00 15 * * ?") // 15:00
    @Scheduled(cron = "0 05 15 * * ?") // 15:05
    public void TareaACiertaHora()
    {
        logger.info("Tarea Automatica programada");
    }
}

