package gob.jmas.model.comercial;

import gob.jmas.model.facturacion.Factura;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "conceptos")
public class Concepto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer clave;
    private Integer conceptoFiscal;
    private String descripcion;
    private Double tasa;
}