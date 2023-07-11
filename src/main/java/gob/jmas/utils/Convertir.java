package gob.jmas.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gob.jmas.dto.facturamaFactura.*;
import gob.jmas.model.facturacion.ConceptoDePago;
import gob.jmas.model.facturacion.Factura;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Convertir {

    private  String cantidadConLetra(String s) {
        StringBuilder result = new StringBuilder();
        BigDecimal totalBigDecimal = new BigDecimal(s).setScale(2, BigDecimal.ROUND_DOWN);
        long parteEntera = totalBigDecimal.toBigInteger().longValue();
        int triUnidades      = (int)((parteEntera % 1000));
        int triMiles         = (int)((parteEntera / 1000) % 1000);
        int triMillones      = (int)((parteEntera / 1000000) % 1000);
        int triMilMillones   = (int)((parteEntera / 1000000000) % 1000);

        if (parteEntera == 0) {
            result.append("Cero ");
            return result.toString();
        }

        if (triMilMillones > 0) result.append(triTexto(triMilMillones).toString() + "Mil ");
        if (triMillones > 0)    result.append(triTexto(triMillones).toString());

        if (triMilMillones == 0 && triMillones == 1) result.append("Millón ");
        else if (triMilMillones > 0 || triMillones > 0) result.append("Millones ");

        if (triMiles > 0)       result.append(triTexto(triMiles).toString() + "Mil ");
        if (triUnidades > 0)    result.append(triTexto(triUnidades).toString());

        return result.toString();
    }
    private StringBuilder triTexto(int n) {
        StringBuilder result = new StringBuilder();
        int centenas = n / 100;
        int decenas  = (n % 100) / 10;
        int unidades = (n % 10);

        switch (centenas) {
            case 0: break;
            case 1:
                if (decenas == 0 && unidades == 0) {
                    result.append("Cien ");
                    return result;
                }
                else result.append("Ciento ");
                break;
            case 2: result.append("Doscientos "); break;
            case 3: result.append("Trescientos "); break;
            case 4: result.append("Cuatrocientos "); break;
            case 5: result.append("Quinientos "); break;
            case 6: result.append("Seiscientos "); break;
            case 7: result.append("Setecientos "); break;
            case 8: result.append("Ochocientos "); break;
            case 9: result.append("Novecientos "); break;
        }

        switch (decenas) {
            case 0: break;
            case 1:
                if (unidades == 0) { result.append("Diez "); return result; }
                else if (unidades == 1) { result.append("Once "); return result; }
                else if (unidades == 2) { result.append("Doce "); return result; }
                else if (unidades == 3) { result.append("Trece "); return result; }
                else if (unidades == 4) { result.append("Catorce "); return result; }
                else if (unidades == 5) { result.append("Quince "); return result; }
                else result.append("Dieci");
                break;
            case 2:
                if (unidades == 0) { result.append("Veinte "); return result; }
                else result.append("Veinti");
                break;
            case 3: result.append("Treinta "); break;
            case 4: result.append("Cuarenta "); break;
            case 5: result.append("Cincuenta "); break;
            case 6: result.append("Sesenta "); break;
            case 7: result.append("Setenta "); break;
            case 8: result.append("Ochenta "); break;
            case 9: result.append("Noventa "); break;
        }

        if (decenas > 2 && unidades > 0)
            result.append("y ");

        switch (unidades) {
            case 0: break;
            case 1: result.append("Un "); break;
            case 2: result.append("Dos "); break;
            case 3: result.append("Tres "); break;
            case 4: result.append("Cuatro "); break;
            case 5: result.append("Cinco "); break;
            case 6: result.append("Seis "); break;
            case 7: result.append("Siete "); break;
            case 8: result.append("Ocho "); break;
            case 9: result.append("Nueve "); break;
        }

        return result;
    }
    public  String DoubleAMxn(double numero) {
        int enteros =Double.valueOf(numero).intValue();
        int decimales = (int) Math.floor((numero - enteros) * 100);
        String cantidadEnLetras=cantidadConLetra(String.valueOf(enteros)).toUpperCase();
        cantidadEnLetras="("+cantidadEnLetras+ "PESOS "+ String.valueOf(decimales)+"/100 MXN)";
        return cantidadEnLetras;
    }

    public String objetoAJsonString(Object objeto) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return objectMapper.writeValueAsString(objeto);
        }
        catch (JsonProcessingException e)
        {
        System.out.println( e.getMessage());
        System.out.println("Hubo un problema con JACKSON y se intentara realizar la operacion con GSON");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(objeto);
        }
    }
    public Cfdi facturaACfdi(@NotNull Factura factura) {
        Cfdi cfdi = new Cfdi();

        cfdi.setNameId(1);//Atributo para especificar el nombre que se establecera en el pdf (default 1 = factura)
        cfdi.setSerie("A"); // Valor fijo "A"
        cfdi.setCurrency("MXN"); // Valor fijo "MXN"
        cfdi.setExpeditionPlace("33800"); // Valor fijo "33800"
        cfdi.setPaymentConditions("CONTADO"); // Valor fijo "CONTADO"
        cfdi.setFolio(factura.getId().toString()); // Usar el atributo id de la entidad Factura
        cfdi.setCfdiType("I"); // Valor fijo "I"
        cfdi.setPaymentForm(factura.getFormaDePago().getClaveSat()); // Usar el atributo formaDePago.claveSat de la entidad Factura
        cfdi.setPaymentMethod("PUE"); // Valor fijo "PUE"
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'00:00:00");
        cfdi.setDate(LocalDateTime.now().format(formatter));//Fecha y hora actual


        //Otros Datos sin obtener aun
        cfdi.setCertNumber(factura.getNoCertificadoSat().toString());
        cfdi.setPaymentTerms("");
        //cfdi.setPaymentAccountNumber("");
        cfdi.setExchangeRate(0.0);
        cfdi.setStatus("");
        cfdi.setOriginalString("");
        cfdi.setOrderNumber("");
        cfdi.setPaymentBankName("");

        // Crear objeto Receiver y establecer valores
        Receiver receiver = new Receiver();
        receiver.setId(factura.getReceptor().getId());
        receiver.setRfc(factura.getReceptor().getRfc());
        receiver.setName(factura.getReceptor().getRazonSocial());
        receiver.setFiscalRegime(factura.getReceptor().getRegimenFiscal().getClave().toString());
        receiver.setCfdiUse(factura.getUsoDeCfdi().getClave()); // Usar el atributo usoDeCfdi.clave de la entidad Factura
        receiver.setTaxZipCode(factura.getReceptor().getCodigoPostal().toString());
        cfdi.setReceiver(receiver);

        if(receiver.getRfc().equals("XAXX010101000"))
        {
            receiver.setCfdiUse("S01");
            GlobalInformation globalInformation= new GlobalInformation();
            globalInformation.setPeriodicity("04");
            globalInformation.setYear(String.format("%04d", LocalDate.now().getYear()));
            globalInformation.setMonths(String.format("%02d", LocalDate.now().getMonthValue()));
            cfdi.setGlobalInformation(globalInformation);
        }

        // Crear lista de Items y establecer valores
        List<Item> items = new ArrayList<>();
        for (ConceptoDePago concepto : factura.getConceptos()) {
            Item item = new Item();
            item.setIdProduct(concepto.getClaveComercial().toString());
            item.setProductCode(concepto.getClaveSat().toString());
            item.setIdentificationNumber(concepto.getClaveComercial().toString());
            item.setDescription(concepto.getDescripcion());
            item.setUnit("Unidad de Servicio");
            item.setUnitCode("E48");
            item.setUnitPrice(concepto.getMonto());
            item.setQuantity(BigDecimal.valueOf(1.0));
            item.setSubtotal(concepto.getMonto());
            item.setDiscount(BigDecimal.valueOf(0.0));

            if(concepto.getTasa().compareTo(BigDecimal.valueOf(0))>0)
            {
                List<Tax> Taxes = new ArrayList<>();
                item.setTaxObject("02");
                Tax tax = new Tax();
                BigDecimal iva= concepto.getMonto().multiply( concepto.getTasa().divide(BigDecimal.valueOf(100)));
                tax.setTotal(iva.setScale(2,RoundingMode.HALF_UP)); // Calcular el total del impuesto
                tax.setName("IVA"); // Valor fijo "IVA"
                tax.setBase(concepto.getMonto()); // Usar el atributo monto de la entidad ConceptoDePago
                tax.setRate(concepto.getTasa().divide(BigDecimal.valueOf(100))); // tasa
                tax.setIsRetention(false); // Valor fijo false
                tax.setIsFederalTax(true);
                tax.setIsQuota(false);
                Taxes.add(tax);
                item.setTaxes(Taxes);
                item.setTotal(item.getSubtotal().add(item.getTaxes().stream()
                        .map(Tax::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)));
            }
            else
            {
                //01 - No objeto de impuesto
                //02 - (Sí objeto de impuesto), se deben desglosar los Impuestos a nivel de Concepto.
                item.setTaxObject("01");
                item.setTotal(item.getSubtotal());
            }
            if(concepto.getMonto().compareTo(BigDecimal.valueOf(0))>0)
                items.add(item);
        }
        cfdi.setItems(items);



        return cfdi;
    }

    public <T> T jsonAModelo(String jsonString, Class<T> clase) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, clase);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            // Manejar la excepción de procesamiento JSON
        }
        return null;
    }

//    public <T> T jsonAModelo(String jsonString, Class<T> clase)
//    {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.readValue(jsonString, clase);
//        } catch (JsonMappingException e) {
//            throw new RuntimeException(e);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}
