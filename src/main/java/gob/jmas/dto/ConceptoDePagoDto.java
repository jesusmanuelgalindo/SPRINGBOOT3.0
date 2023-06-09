package gob.jmas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConceptoDePagoDto {
    public ConceptoDePagoDto(Integer claveComercial, Integer claveSat, String descripcion, Double monto, Double tasa) {
        this.claveComercial = claveComercial;
        this.claveSat = claveSat;
        this.descripcion = descripcion;
        this.monto = monto;
        this.tasa = tasa;
    }

    private Integer claveComercial;
    private Integer claveSat;
    private String descripcion;
    private Double monto;
    private Double tasa;
}
