package gob.jmas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import gob.jmas.model.facturacion.Factura;
import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.UsoDeCfdi;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class FacturaDto {

    private Integer id;
    private String uuid;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "UTC")
    private LocalDateTime fechaFacturacion;
    private UsoDeCfdi usoDeCfdi;
    private Receptor receptor;
    private String xml;
    private String cadenaOriginal;
    private String selloDigitalCfdi;
    private String selloDelSat;
    private String cuenta;
    private String nombre;
    private String direccion;
    private Integer caja;
    private Integer referencia;
    private FormaDePago formaDePago;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private LocalDate fechaDePago;

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "UTC")
    private LocalDateTime fechaDeCertificacion;
    private Integer noCertificadoEmisor;
    private Integer noCertificadoSat;
    private String emailRegistrado;
    private String emailAdicional;
    private Boolean activa;

    public FacturaDto(Factura factura) {
        this.id = factura.getId();
        this.uuid = factura.getUuid();
        this.fechaFacturacion = factura.getFechaFacturacion();
        this.usoDeCfdi = factura.getUsoDeCfdi();
        this.receptor = factura.getReceptor();
        this.xml = factura.getXml();
        this.cadenaOriginal = factura.getCadenaOriginal();
        this.selloDigitalCfdi = factura.getSelloDigitalCfdi();
        this.selloDelSat = factura.getSelloDelSat();
        this.cuenta = factura.getCuenta();
        this.nombre = factura.getNombre();
        this.direccion = factura.getDireccion();
        this.caja = factura.getCaja();
        this.referencia = factura.getReferencia();
        this.formaDePago = factura.getFormaDePago();
        this.fechaDePago = factura.getFechaDePago();
        this.fechaDeCertificacion = factura.getFechaDeCertificacion();
        this.noCertificadoEmisor = factura.getNoCertificadoEmisor();
        this.noCertificadoSat = factura.getNoCertificadoSat();
        this.emailRegistrado = factura.getEmailRegistrado();
        this.emailAdicional = factura.getEmailAdicional();
        this.activa = factura.getActiva();
    }

}
