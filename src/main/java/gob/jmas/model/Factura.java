package gob.jmas.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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
    private Integer id;

    @Column(nullable = false)
    private Integer noFactura;
    @Column(nullable = false, length = 36)
    private String uuid;
    private LocalDateTime fechaFacturacion;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uso_de_cfdi_id")
    private UsoDeCfdi usoDeCfdi;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receptor_id")
    private Receptor receptor;
    @Column(columnDefinition = "Text")
    private String xml;
    @Column(nullable = false, length = 10)
    private String cuenta;
    @Column(nullable = false, length = 255)
    private String nombre;
    @Column(nullable = false)
    private Integer caja;
    @Column(nullable = false, length = 10)
    private Integer referencia;
    private Double monto;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "forma_de_pago_id")
    private FormaDePago formaDePago;
    private LocalDate fechaDePago;
    private String emailRegistrado;
    private String emailAdicional;
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConceptoDePago> conceptos;
}