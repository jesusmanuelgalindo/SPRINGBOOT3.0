package gob.jmas.model.facturacion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "conceptos_de_pago")
public class ConceptoDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "facturas_id")
    private Factura factura;
    private Integer claveComercial;
    private Integer claveSat;
    private String descripcion;
    private BigDecimal monto;
    private BigDecimal tasa;
}