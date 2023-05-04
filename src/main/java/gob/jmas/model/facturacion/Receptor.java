package gob.jmas.model.facturacion;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReceptor;
    private String rfc;
    private String razonSocial;
    private Integer codigoPostal;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "regimen_fiscal_id")
    private RegimenFiscal regimenFiscal;
    private String email;

}