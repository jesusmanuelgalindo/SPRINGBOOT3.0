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
    private HttpStatus httpStatus;
    private T datos;
    private Integer numeroDeRegistros;
    private String mensaje;

    private static final Logger logger = LoggerFactory.getLogger(Respuesta.class);
    public Respuesta( String endPoint,Integer idUsuario, HttpStatus httpStatus, T datos,Integer numeroDeRegistros, String mensaje) {

        this.httpStatus = httpStatus;
        this.numeroDeRegistros = numeroDeRegistros;
        this.mensaje = mensaje;
        this.datos = datos;
        String bitacora="\n"+
                        "----------------------SOLICITUD A ENDPOINT---------------------------"+"\n"+
                        "Solicitud Procesada : "+endPoint+"\n"+
                        "Usuario que Solicita: "+idUsuario.toString()+"\n"+
                        "Status              : "+httpStatus.toString() +"\n"+
                        "Registros enviados  : "+numeroDeRegistros.toString()+"\n"+
                        "Mensaje             : "+mensaje+"\n"+
                        "---------------------------------------------------------------------"+"\n";
        switch (httpStatus)
        {
            case INTERNAL_SERVER_ERROR:
            {
                logger.error(bitacora);
                break;
            }
            case BAD_REQUEST:
            case UNAUTHORIZED:
            {
                logger.warn(bitacora);
                break;
            }
            default:
            {
                logger.info(bitacora);
                break;
            }

        }
    }
}