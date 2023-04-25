package gob.jmas.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "concepto_de_pago")
public class ConceptoDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_no_factura")
    private Factura factura;

    private Integer clave;

    private Integer conceptoFiscal;

    private String descripcion;

    private Double monto;

    private Double tasa;

    // Constructores, getters y setters
}