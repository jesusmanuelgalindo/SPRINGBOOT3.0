package gob.jmas.service.factura;

import gob.jmas.dto.ConceptoDePagoDto;
import gob.jmas.dto.FacturaDto;
import gob.jmas.dto.NuevaFacturaDto;
import gob.jmas.dto.PagoDto;
import gob.jmas.model.comercial.Pago;
import gob.jmas.model.facturacion.*;
import gob.jmas.repository.comercial.PagoRepository;
import gob.jmas.repository.facturacion.FacturaRepository;
import gob.jmas.service.formaDePago.FormaDePagoService;
import gob.jmas.service.receptor.ReceptorService;
import gob.jmas.service.usoDeCfdi.UsoDeCfdiService;
import gob.jmas.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.NonUniqueResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaRepository facturaRepository;
    @Autowired
    private PagoRepository pagoRepository;
    @Autowired
    private EnviarEmail enviarEmail;

    @Autowired
    UsoDeCfdiService usoDeCfdiService;

    @Autowired
    private FormaDePagoService formaDePagoService;

    @Autowired
    ReceptorService receptorService;

    @Autowired
    Censurar censurar;

    @Autowired
    Convertir convertir;
    @Override
    public PagoDto  detalleDePago(String cuenta, Integer caja, Integer referencia ) throws Excepcion {

        //Se trae los registros de la base de datos
        List<Pago> pagos = pagoRepository.consultarDetalleDePago(cuenta, caja, referencia);

        if (pagos.size() == 0)
            return null;

        //Verifica si no ha sido facturado con anterioridad
        Factura facturaLocalizada = findFacturaByCuentaCajaReferencia(cuenta, caja, referencia);
        if (facturaLocalizada != null)
            throw new Excepcion(HttpStatus.ALREADY_REPORTED, "El Ticket #" + referencia + " ya fue facturado con anterioridad y la factura fue enviada a: " + censurar.censurarEmail(facturaLocalizada.getEmailRegistrado()) + (facturaLocalizada.getEmailAdicional().length() > 0 ? " y a " + censurar.censurarEmail(facturaLocalizada.getEmailAdicional()) : ""));

        //Verifica que la fecha del ticket sea del mes actual
        if (pagos.get(0).getFechaDePago().getMonthValue() != LocalDate.now().getMonthValue() || pagos.get(0).getFechaDePago().getYear() != LocalDate.now().getYear())
            throw new Excepcion(HttpStatus.FORBIDDEN, "El ticket de pago corresponde a un mes anterior");


        //Prepara los datos que va devolver
        PagoDto pagoDto = new PagoDto();
        pagoDto.setCuenta(pagos.get(0).getCuenta());
        pagoDto.setNombre(pagos.get(0).getNombre());
        pagoDto.setDireccion(pagos.get(0).getDireccion());
        pagoDto.setCaja(pagos.get(0).getCaja());
        pagoDto.setReferencia(pagos.get(0).getReferencia());

        FormaDePago formaDePago = formaDePagoService.getFormaDePagoByClaveComercial(pagos.get(0).getFormaDePago());
        if (formaDePago == null)
            throw new Excepcion(HttpStatus.NOT_FOUND, "La forma de Pago '" + pagos.get(0).getFormaDePago() + "' registrada en el Ticket,no existe en el Catálogo del SAT. Intente nuevamente. Si el problema persiste reportelo a JMAS Parral");
        else
            pagoDto.setFormaDePago(formaDePago);


        pagoDto.setFechaDePago(pagos.get(0).getFechaDePago());

        List<ConceptoDePagoDto> conceptosDePagoDto = new ArrayList<>();

        for (Pago pago : pagos) {
            ConceptoDePagoDto conceptoDePagoDto = new ConceptoDePagoDto();
            conceptoDePagoDto.setClaveComercial(pago.getClaveComercial());
            conceptoDePagoDto.setClaveSat(pago.getClaveSat());
            conceptoDePagoDto.setDescripcion(pago.getDescripcion());
            conceptoDePagoDto.setMonto(pago.getMonto());
            conceptoDePagoDto.setTasa(pago.getTasa());
            conceptosDePagoDto.add(conceptoDePagoDto);
        }
        pagoDto.setConceptos(conceptosDePagoDto);
        return pagoDto;
    }

    @Override
    public Factura findFacturaByCuentaCajaReferencia(String cuenta, Integer caja, Integer referencia)
    {
        Optional<Factura> optionalFactura = facturaRepository.findFacturaByCuentaCajaReferencia(cuenta, caja, referencia);
        return optionalFactura.orElse(null);
    }

    @Override
    public Factura getFacturaById(Integer id) {
        Optional<Factura> optionalFactura = facturaRepository.findById(id);
        return optionalFactura.orElse(null);
    }

    @Override
    public List<Factura> getAllFacturas() {
        return facturaRepository.findAll();
    }

    @Override
    public Factura createFactura(Factura facturaNueva) {
        return facturaRepository.save(facturaNueva);
    }
    @Override
    public Factura createFactura(NuevaFacturaDto nuevaFacturaDto)
    {
        //Localiza el pago en BD Comercial
        PagoDto pagoDto= detalleDePago(nuevaFacturaDto.getCuenta(), nuevaFacturaDto.getCaja(), nuevaFacturaDto.getReferencia());
        if(pagoDto==null)
            throw new Excepcion(HttpStatus.NOT_FOUND,"No existe ningun registro de pago en la base de datos que coincida con los datos ingresados (CAJA:'"+nuevaFacturaDto.getCaja().toString()+"',REFERENCIA:'"+nuevaFacturaDto.getReferencia().toString()+"',CUENTA:'"+nuevaFacturaDto.getCuenta()+"')");

        //Localiza el Receptor en la BD Facturacion
        Receptor receptor=receptorService.getReceptorById(nuevaFacturaDto.getIdReceptor());
        if(receptor==null)
            throw new Excepcion(HttpStatus.NOT_FOUND, "El id del Receptor '" + nuevaFacturaDto.getIdReceptor() + "' no existe en la base de datos. Intente nuevamente. Si el problema persiste reportelo a JMAS Parral");

        //Forma la estructura de la nueva factura
        Factura nueva= new Factura();


        UsoDeCfdi usoDeCfdi=usoDeCfdiService.getUsoDeCfdiById(nuevaFacturaDto.getIdUsoDeCfdi());
        if(usoDeCfdi==null)
            throw new Excepcion(HttpStatus.NOT_FOUND, "El uso del CFDI con clave '" + nuevaFacturaDto.getIdUsoDeCfdi() + "' no existe en la base de datos. Intente nuevamente. Si el problema persiste reportelo a JMAS Parral");

        nueva.setUsoDeCfdi(usoDeCfdi);
        nueva.setCuenta(pagoDto.getCuenta());
        nueva.setNombre(pagoDto.getNombre());
        nueva.setDireccion(pagoDto.getDireccion());
        nueva.setCaja(pagoDto.getCaja());
        nueva.setReferencia(pagoDto.getReferencia());
        nueva.setFormaDePago(pagoDto.getFormaDePago());
        nueva.setFechaDePago(pagoDto.getFechaDePago());
        nueva.setReceptor(receptor);
        nueva.setEmailRegistrado(receptor.getEmail());
        nueva.setEmailAdicional(nuevaFacturaDto.getEmailAdicional());
        nueva.setActiva(false);



        //Esto lo va obtener del servicio del PAC
        nueva.setUuid("SIN TIMBRAR");
        nueva.setNoCertificadoEmisor(0);
        nueva.setNoCertificadoSat(0);
        nueva.setFechaFacturacion(LocalDate.now().atStartOfDay());
        nueva.setFechaDeCertificacion(LocalDateTime.now());
        nueva.setXml("SIN TIMBRAR");
        nueva.setCadenaOriginal("SIN TIMBRAR");
        nueva.setSelloDigitalCfdi("SIN TIMBRAR");
        nueva.setSelloDelSat("SIN TIMBRAR");







        // Recorrido de los conceptos de pago en pagoDto
        for (ConceptoDePagoDto conceptoDto : pagoDto.getConceptos()) {
            // Crear una instancia de ConceptoDePago
            ConceptoDePago concepto = new ConceptoDePago();

            // Asignar los valores correspondientes del conceptoDto al concepto
            concepto.setClaveComercial(conceptoDto.getClaveComercial());
            concepto.setClaveSat(conceptoDto.getClaveSat());
            concepto.setDescripcion(conceptoDto.getDescripcion());
            concepto.setMonto(conceptoDto.getMonto());
            concepto.setTasa(conceptoDto.getTasa());

            // Asignar la factura actual como la factura del concepto
            concepto.setFactura(nueva);

            // Agregar el concepto a la lista de conceptos en nueva
            nueva.getConceptos().add(concepto);
        }


        return facturaRepository.save(nueva);

    }

    @Override
    public Factura updateFactura(Integer id, Factura facturaActualizada) {
        // Buscamos la factura a actualizar
        Optional<Factura> optionalFactura = facturaRepository.findById(id);

        if (optionalFactura.isPresent()) {
            Factura facturaExistente = optionalFactura.get();

            // Actualizamos los campos de la factura existente con los valores de la factura actualizada
            facturaExistente.setUuid(facturaActualizada.getUuid());
            facturaExistente.setFechaFacturacion(facturaActualizada.getFechaFacturacion());
            facturaExistente.setUsoDeCfdi(facturaActualizada.getUsoDeCfdi());
            facturaExistente.setReceptor(facturaActualizada.getReceptor());
            facturaExistente.setXml(facturaActualizada.getXml());
            facturaExistente.setCuenta(facturaActualizada.getCuenta());
            facturaExistente.setNombre(facturaActualizada.getNombre());
            facturaExistente.setCaja(facturaActualizada.getCaja());
            facturaExistente.setReferencia(facturaActualizada.getReferencia());
            facturaExistente.setFormaDePago(facturaActualizada.getFormaDePago());
            facturaExistente.setFechaDePago(facturaActualizada.getFechaDePago());
            facturaExistente.setEmailRegistrado(facturaActualizada.getEmailRegistrado());
            facturaExistente.setEmailAdicional(facturaActualizada.getEmailAdicional());
            facturaExistente.setConceptos(facturaActualizada.getConceptos());

            // Guardamos los cambios en la base de datos y retornamos la factura actualizada
            return facturaRepository.save(facturaExistente);
        }
        else
        {
            throw new Excepcion(HttpStatus.NOT_FOUND,"Factura NO Encontrada. Intente nuevamente. Si el problema persiste reportelo a JMAS Parral");
        }
    }

    @Override
    public void deleteFactura(Integer id) {
        Optional<Factura> optionalFactura = facturaRepository.findById(id);
        if (optionalFactura.isPresent()) {
            facturaRepository.delete(optionalFactura.get());
        } else {
            throw new Excepcion(HttpStatus.NOT_FOUND,"Factura NO Encontrada. Intente nuevamente. Si el problema persiste reportelo a JMAS Parral");

        }
    }
    @Override
    public void EnviarFactura(Factura factura) {
        String asunto="Factura No. "+factura.getId().toString()+ " JMAS Parral";
        String mensaje="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "<head>\n" +
                "<!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "  <o:OfficeDocumentSettings>\n" +
                "    <o:AllowPNG/>\n" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "  </o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]-->\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                "  <title></title>\n" +
                "  \n" +
                "    <style type=\"text/css\">\n" +
                "      @media only screen and (min-width: 520px) {\n" +
                "  .u-row {\n" +
                "    width: 500px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    vertical-align: top;\n" +
                "  }\n" +
                "\n" +
                "  .u-row .u-col-50 {\n" +
                "    width: 250px !important;\n" +
                "  }\n" +
                "\n" +
                "  .u-row .u-col-100 {\n" +
                "    width: 500px !important;\n" +
                "  }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "@media (max-width: 520px) {\n" +
                "  .u-row-container {\n" +
                "    max-width: 100% !important;\n" +
                "    padding-left: 0px !important;\n" +
                "    padding-right: 0px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    min-width: 320px !important;\n" +
                "    max-width: 100% !important;\n" +
                "    display: block !important;\n" +
                "  }\n" +
                "  .u-row {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col > div {\n" +
                "    margin: 0 auto;\n" +
                "  }\n" +
                "}\n" +
                "body {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "\n" +
                "table,\n" +
                "tr,\n" +
                "td {\n" +
                "  vertical-align: top;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                ".ie-container table,\n" +
                ".mso-container table {\n" +
                "  table-layout: fixed;\n" +
                "}\n" +
                "\n" +
                "* {\n" +
                "  line-height: inherit;\n" +
                "}\n" +
                "\n" +
                "a[x-apple-data-detectors='true'] {\n" +
                "  color: inherit !important;\n" +
                "  text-decoration: none !important;\n" +
                "}\n" +
                "\n" +
                "table, td { color: #000000; } #u_body a { color: #0000ee; text-decoration: underline; }\n" +
                "    </style>\n" +
                "  \n" +
                "  \n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #e7e7e7;color: #000000\">\n" +
                "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
                "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                "  <table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #e7e7e7;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "  <tbody>\n" +
                "  <tr style=\"vertical-align: top\">\n" +
                "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #e7e7e7;\"><![endif]-->\n" +
                "    \n" +
                "\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"500\" style=\"width: 500px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "          <span>&#160;</span>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tr>\n" +
                "    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
                "      \n" +
                "      <img align=\"center\" border=\"0\" src=\"https://www.jmasparral.gob.mx/imagenes/logo.png\" alt=\"\" title=\"\" style=\"outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;clear: both;display: inline-block !important;border: none;height: auto;float: none;width: 100%;max-width: 480px;\" width=\"480\"/>\n" +
                "      \n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"500\" style=\"width: 500px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 500px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "          <span>&#160;</span>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <h1 style=\"margin: 0px; line-height: 140%; text-align: left; word-wrap: break-word; font-size: 22px; font-weight: 400;\"><strong>Tu factura se ha generado correctamente</strong></h1>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "          <span>&#160;</span>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <h4 style=\"margin: 0px; line-height: 140%; text-align: left; word-wrap: break-word; font-size: 16px; font-weight: 400;\"><strong>Datos de la Factura</strong></h4>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                "    <p style=\"line-height: 140%;\"><strong>No. Factura       : </strong>"+factura.getId()+"</p>\n" +
                "    <p style=\"line-height: 140%;\"><strong>UUID                 : </strong>"+factura.getUuid()+"</p>\n" +
                "<p style=\"line-height: 140%;\"><strong>Fecha del Pago: </strong>"+factura.getFechaDePago()+"</p>\n" +
                "<p style=\"line-height: 140%;\"><strong>Monto Total      : </strong>$ SIN CALCULAR AUN</p>\n" +
                "<p style=\"line-height: 140%;\"><strong>Forma de Pago: </strong>"+factura.getFormaDePago().getDescripcion()+"</p>\n" +
                "<p style=\"line-height: 140%;\"><strong>Uso del CFDI    : </strong>"+factura.getUsoDeCfdi()+"</p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "          <span>&#160;</span>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <h4 style=\"margin: 0px; line-height: 140%; text-align: left; word-wrap: break-word; font-size: 16px; font-weight: 400;\"><strong>Datos del Receptor</strong></h4>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                "    <p style=\"line-height: 140%;\"><strong>RFC: </strong>XAX010101ABC<strong><br />Razón Social: </strong>NOMBRE DEL CONTRIBUYENTE</p>\n" +
                "<p style=\"line-height: 140%;\"><strong>Codigo Postal:</strong> 33800</p>\n" +
                "<p style=\"line-height: 140%;\"><strong>Régimen Fiscal:</strong> REGIMEN SIMPLIFICADO DE CONFIANZA</p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <table height=\"0px\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;border-top: 1px solid #BBBBBB;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "    <tbody>\n" +
                "      <tr style=\"vertical-align: top\">\n" +
                "        <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top;font-size: 0px;line-height: 0px;mso-line-height-rule: exactly;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%\">\n" +
                "          <span>&#160;</span>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div style=\"font-size: 14px; line-height: 140%; text-align: left; word-wrap: break-word;\">\n" +
                "    <p style=\"line-height: 140%;\">Tu factura ya está registrada en el SAT, puedes obtenerla desde el portal del SAT, o si asi lo deseas Descarga los archivos dando clic en los siguientes enlaces:</p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "  <div class=\"u-row\" style=\"Margin: 0 auto;min-width: 320px;max-width: 500px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: transparent;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:500px;\"><tr style=\"background-color: transparent;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"250\" style=\"width: 250px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 250px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->\n" +
                "<div align=\"center\">\n" +
                "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"\" style=\"height:37px; v-text-anchor:middle; width:136px;\" arcsize=\"11%\"  stroke=\"f\" fillcolor=\"#3AAEE0\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:arial,helvetica,sans-serif;\"><![endif]-->  \n" +
                "    <a href=\"\" target=\"_blank\" class=\"v-button\" style=\"box-sizing: border-box;display: inline-block;font-family:arial,helvetica,sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #3AAEE0; border-radius: 4px;-webkit-border-radius: 4px; -moz-border-radius: 4px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">\n" +
                "      <span style=\"display:block;padding:10px 20px;line-height:120%;\"><span style=\"line-height: 16.8px;\">Descargar PDF</span></span>\n" +
                "    </a>\n" +
                "  <!--[if mso]></center></v:roundrect><![endif]-->\n" +
                "</div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"250\" style=\"width: 250px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-50\" style=\"max-width: 320px;min-width: 250px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td style=\"overflow-wrap:break-word;word-break:break-word;padding:10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <!--[if mso]><style>.v-button {background: transparent !important;}</style><![endif]-->\n" +
                "<div align=\"center\">\n" +
                "  <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"\" style=\"height:37px; v-text-anchor:middle; width:137px;\" arcsize=\"11%\"  stroke=\"f\" fillcolor=\"#3AAEE0\"><w:anchorlock/><center style=\"color:#FFFFFF;font-family:arial,helvetica,sans-serif;\"><![endif]-->  \n" +
                "    <a href=\"\" target=\"_blank\" class=\"v-button\" style=\"box-sizing: border-box;display: inline-block;font-family:arial,helvetica,sans-serif;text-decoration: none;-webkit-text-size-adjust: none;text-align: center;color: #FFFFFF; background-color: #3AAEE0; border-radius: 4px;-webkit-border-radius: 4px; -moz-border-radius: 4px; width:auto; max-width:100%; overflow-wrap: break-word; word-break: break-word; word-wrap:break-word; mso-border-alt: none;font-size: 14px;\">\n" +
                "      <span style=\"display:block;padding:10px 20px;line-height:120%;\"><span style=\"line-height: 16.8px;\">Descargar XML</span></span>\n" +
                "    </a>\n" +
                "  <!--[if mso]></center></v:roundrect><![endif]-->\n" +
                "</div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "  </tbody>\n" +
                "  </table>\n" +
                "  <!--[if mso]></div><![endif]-->\n" +
                "  <!--[if IE]></div><![endif]-->\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";


        if(factura.getEmailRegistrado().length()>0)
            enviarEmail.enviarEmailHTML(factura.getEmailRegistrado(),asunto,mensaje);

        if(factura.getEmailAdicional().length()>0)
            enviarEmail.enviarEmailHTML(factura.getEmailAdicional(),asunto,mensaje);

    }
}