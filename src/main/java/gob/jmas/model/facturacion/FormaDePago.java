package gob.jmas.model.facturacion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cat_formas_de_pago")
public class FormaDePago {

    public FormaDePago(Integer id)
    {
        this.setId(id);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String claveComercial;

    private String claveSat;

    private String descripcion;

}