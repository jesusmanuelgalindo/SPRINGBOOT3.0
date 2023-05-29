package gob.jmas.model.facturacion;

import gob.jmas.dto.ReceptorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cat_receptores")
public class Receptor {

    public Receptor(Integer id)
    {
        this.setId(id);
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String rfc;
    private String razonSocial;
    private Integer codigoPostal;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cat_regimen_fiscal_id")
    private RegimenFiscal regimenFiscal;
    private String email;

}