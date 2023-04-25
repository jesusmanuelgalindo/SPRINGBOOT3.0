package gob.jmas.utils;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TareasAutomaticas {

    private static final Logger logger = LoggerFactory.getLogger(TareasAutomaticas.class);

    @Scheduled(fixedRate = 3600000) //Cada Hora
    public void CadaHora()
    {
        logger.info("Tarea Automatica programada");

    }

    @Scheduled(cron = "0 00 15 * * ?") // 15:00
    @Scheduled(cron = "0 05 15 * * ?") // 15:05
    public void TareaACiertaHora() {

        logger.info("Tarea Automatica programada");
    }
}

