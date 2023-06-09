package gob.jmas.model.facturacion;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cat_usos_de_cfdi")
public class UsoDeCfdi {

    public UsoDeCfdi(Integer id)
    {
        this.setId(id);
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String clave;

    private String descripcion;

}
