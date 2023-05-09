package gob.jmas.config;

import gob.jmas.model.facturacion.Factura;
import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.service.factura.FacturaService;
import gob.jmas.utils.Convertir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Month;

@Configuration
@EnableScheduling
@Component
public class TareasAutomaticasConfig
{
    @Autowired
    FacturaService facturaService;

    @Autowired
    Convertir convertir;

    private static final Logger logger = LoggerFactory.getLogger(TareasAutomaticasConfig.class);

    @Scheduled(fixedRate = 3600000) //Cada Hora
    public void CadaHora()
    {
        logger.info("Tarea Automatica INICIADA");
        Factura ejemplo= new Factura();
        ejemplo.setId(999);
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
