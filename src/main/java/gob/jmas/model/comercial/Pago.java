package gob.jmas.model.comercial;


import gob.jmas.model.facturacion.ConceptoDePago;
import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.UsoDeCfdi;
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
@Table(name = "pagos")
public class Pago {

    public Pago(Integer id)
    {
        this.setId(id);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 36)
    private String uuid;

    private LocalDateTime fechaFacturacion;


    @Column(columnDefinition = "Text")
    private String xml;
    @Column(columnDefinition = "Text")
    private String cadenaOriginal;
    @Column(columnDefinition = "Text")
    private String selloDigitalCfdi;
    @Column(columnDefinition = "Text")
    private String selloDelSat;
    @Column(nullable = false, length = 10)
    private String cuenta;
    @Column(nullable = false, length = 255)
    private String nombre;
    @Column(nullable = false, length = 255)
    private String direccion;
    @Column(nullable = false, length = 255)
    private String colonia;
    @Column(nullable = false)
    private Integer caja;
    @Column(nullable = false, length = 10)
    private Integer referencia;
    private Double monto;
    private String formaDePago;
    private LocalDate fechaDePago;
    private LocalDateTime fechaDeCertificacion;
    @Column(nullable = false, length = 20)
    private Integer noCertificadoEmisor;
    @Column(nullable = false, length = 20)
    private Integer noCertificadoSat;
    private String emailRegistrado;
    private String emailAdicional;
}