package gob.jmas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gob.jmas.model.facturacion.FormaDePago;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagoDto {
    public PagoDto(String cuenta, String nombre, String direccion, Integer caja, Integer referencia, FormaDePago formaDePago, LocalDate fechaDePago,List<ConceptoDePagoDto> conceptos) {
        this.cuenta = cuenta;
        this.nombre = nombre;
        this.direccion = direccion;
        this.caja = caja;
        this.referencia = referencia;
        this.formaDePago = formaDePago;
        this.fechaDePago = fechaDePago;
        this.conceptos=conceptos;
    }

    private String cuenta;
    private String nombre;
    private String direccion;
    private Integer caja;
    private Integer referencia;
    private FormaDePago formaDePago;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate fechaDePago;
    List<ConceptoDePagoDto> conceptos;
}
