package gob.jmas.model.comercial;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String formaDePago;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate fechaDePago;
    private Integer claveComercial;
    private Integer claveSat;
    private String descripcion;
    private Double monto;
    private Double tasa;

}