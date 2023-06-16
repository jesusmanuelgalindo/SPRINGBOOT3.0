package gob.jmas.dto;

import gob.jmas.model.facturacion.Receptor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

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
    @Digits(integer = 9, fraction = 0, message = "El id debe ser un número entero")
    private Integer id;
    @Pattern(regexp = "^[A-Za-z]{3,4}\\d{6}[A-Za-z0-9]{2}\\d$", message = "No Ingreso un RFC Valido. Verifique el formato")
    private String rfc;
    private String razonSocial;

    @Min(value = 10000, message = "El código postal debe ser un número entero de 6 dígitos")
    @Max(value = 99999, message = "El código postal debe ser un número entero de 6 dígitos")
    private Integer codigoPostal;
    @Digits(integer = 9, fraction = 0, message = "No Ingreso un Identificador de Regimen Fiscal del menu de opciones")
    private Integer idRegimenFiscal;
    private String regimenFiscal;
    @Pattern(regexp = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$", message = "No Ingreso un Email Valido. Verifique el formato")
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
