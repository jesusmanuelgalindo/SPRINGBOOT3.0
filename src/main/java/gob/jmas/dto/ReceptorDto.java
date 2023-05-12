package gob.jmas.dto;

import gob.jmas.model.facturacion.Receptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReceptorDto {

    private Integer idReceptor;
    private String rfc;
    private String razonSocial;
    private Integer codigoPostal;
    private Integer idRegimenFiscal;
    private String regimenFiscal;
    private String email;

    public ReceptorDto(Receptor receptor)
    {
       this.idReceptor=receptor.getIdReceptor();
       this.rfc=receptor.getRfc();
       this.razonSocial=receptor.getRazonSocial();
       this.codigoPostal=receptor.getCodigoPostal();
       this.idRegimenFiscal=receptor.getRegimenFiscal().getId();
       this.regimenFiscal=receptor.getRegimenFiscal().getDescripcion();
       this.email=receptor.getEmail();
    }
}
