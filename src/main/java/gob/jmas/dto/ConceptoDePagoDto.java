package gob.jmas.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConceptoDePagoDto {
    public ConceptoDePagoDto(Integer clave, Integer conceptoFiscal, String concepto, Double monto, Double tasa) {
        this.clave = clave;
        this.conceptoFiscal = conceptoFiscal;
        this.concepto = concepto;
        this.monto = monto;
        this.tasa = tasa;
    }

    private Integer clave;
    private Integer conceptoFiscal;
    private String concepto;
    private Double monto;
    private Double tasa;
}
