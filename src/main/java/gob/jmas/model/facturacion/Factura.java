package gob.jmas.model.facturacion;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "facturas")
public class Factura {

    public Factura(Integer id)
    {
        this.setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 36)
    private String uuid;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm", timezone = "UTC")
    private LocalDateTime fechaFacturacion;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uso_de_cfdi_id")
    private UsoDeCfdi usoDeCfdi;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "receptor_id")
    private Receptor receptor;
    @Column(columnDefinition = "Text")
    private String xml;
    @Column(columnDefinition = "Text")
    private String cadenaOriginal;
    @Column(columnDefinition = "Text ")
    private String selloDigitalCfdi;
    @Column(columnDefinition = "Text ")
    private String selloDelSat;
    @Column(nullable = false, length = 10)
    private String cuenta;
    @Column(nullable = false, length = 255)
    private String nombre;
    @Column(nullable = false, length = 255)
    private String direccion;
    @Column(nullable = false)
    private Integer caja;
    @Column(nullable = false, length = 10)
    private Integer referencia;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "forma_de_pago_id")
    private FormaDePago formaDePago;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate fechaDePago;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "UTC")
    private LocalDateTime fechaDeCertificacion;
    @Column(nullable = false, length = 20)
    private Integer noCertificadoEmisor;
    @Column(nullable = false, length = 20)
    private Integer noCertificadoSat;
    private String emailRegistrado;
    private String emailAdicional;
    private Boolean activa;
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConceptoDePago> conceptos;
}