package gob.jmas.dto;

import gob.jmas.model.facturacion.Receptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceptorDto {

    public ReceptorDto(ReceptorDto receptor)
    {
        this.id=receptor.getId();
        this.rfc=receptor.getRfc();
        this.razonSocial=receptor.getRazonSocial();
        this.codigoPostal=receptor.getCodigoPostal();
        this.idRegimenFiscal=receptor.getIdRegimenFiscal();
        this.regimenFiscal=receptor.getRegimenFiscal();
        this.email=receptor.getEmail();
    }
    private Integer id;
    private String rfc;
    private String razonSocial;
    private Integer codigoPostal;
    private Integer idRegimenFiscal;
    private String regimenFiscal;
    private String email;

    public ReceptorDto(Receptor receptor)
    {
       this.id=receptor.getId();
       this.rfc=receptor.getRfc();
       this.razonSocial=receptor.getRazonSocial();
       this.codigoPostal=receptor.getCodigoPostal();
       this.idRegimenFiscal=receptor.getRegimenFiscal().getId();
       this.regimenFiscal=receptor.getRegimenFiscal().getDescripcion();
       this.email=receptor.getEmail();
    }
}
