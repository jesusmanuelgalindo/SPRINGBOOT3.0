package gob.jmas.model;

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
    private String regimenFiscal;
    private String email;

}