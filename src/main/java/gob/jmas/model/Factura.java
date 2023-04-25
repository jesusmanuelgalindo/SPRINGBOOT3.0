package gob.jmas.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "factura")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer noFactura;

    private String uuid;

    private Integer fechaFacturacion;

    private String usoDeCfdi;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receptor_id")
    private Receptor receptor;

    private String xml;

    private String cuenta;

    private String nombre;

    private Integer caja;

    private Integer referencia;

    private Double monto;

    private String formaPago;

    private LocalDateTime fechaPago;

    private String emailRegistrado;

    private String emailAdicional;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<ConceptoDePago> conceptos;

    // Constructores, getters y setters
}