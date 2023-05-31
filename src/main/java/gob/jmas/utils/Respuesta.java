package gob.jmas.utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
@Getter
@Setter
@NoArgsConstructor
public class Respuesta<T>
{

    private T datos;
    private Integer numeroDeRegistros;
    private String mensaje;

    private static final Logger logger = LoggerFactory.getLogger(Respuesta.class);
    public Respuesta( T datos,Integer numeroDeRegistros, String mensaje) {
        this.numeroDeRegistros = numeroDeRegistros;
        this.mensaje = mensaje;
        this.datos = datos;
        String bitacora="\n"+
                        "----------------------SOLICITUD A ENDPOINT---------------------------"+"\n"+
                        "Registros enviados  : "+numeroDeRegistros.toString()+"\n"+
                        "Mensaje             : "+mensaje+"\n"+
                        "---------------------------------------------------------------------"+"\n";
        logger.info(bitacora);

    }
}