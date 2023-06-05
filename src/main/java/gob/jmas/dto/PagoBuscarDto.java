package gob.jmas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PagoBuscarDto {
    private String cuenta;
    private Integer caja;
    private Integer referencia;
}
