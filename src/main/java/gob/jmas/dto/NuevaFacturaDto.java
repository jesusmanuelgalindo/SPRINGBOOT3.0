package gob.jmas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NuevaFacturaDto {

    Integer idReceptor;
    String cuenta;
    Integer caja;
    Integer referencia;
    Integer idUsoDeCfdi;
    String emailAdicional;


}
