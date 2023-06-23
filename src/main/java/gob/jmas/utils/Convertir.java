package gob.jmas.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gob.jmas.dto.facturamaFactura.FacturamaFactura;
import gob.jmas.dto.facturamaFactura.Item;
import gob.jmas.dto.facturamaFactura.Receiver;
import gob.jmas.dto.facturamaFactura.Tax;
import gob.jmas.model.facturacion.ConceptoDePago;
import gob.jmas.model.facturacion.Factura;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.*;
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

    public String objetoAJsonString(Object objeto)
    {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        // Convertir el objeto a JSON
        String jsonString = gson.toJson(objeto);

        return jsonString;
    }

    public FacturamaFactura facturaAFacturamaFactura(@NotNull Factura factura) {
        FacturamaFactura facturamaFactura= new FacturamaFactura();

        facturamaFactura.setSerie("A"); // Valor fijo "A"
        facturamaFactura.setCurrency("MXN"); // Valor fijo "MXN"
        facturamaFactura.setExpeditionPlace("33800"); // Valor fijo "33800"
        facturamaFactura.setPaymentConditions("CONTADO"); // Valor fijo "CONTADO"
        facturamaFactura.setFolio(factura.getId().toString()); // Usar el atributo id de la entidad Factura
        facturamaFactura.setCfdiType("I"); // Valor fijo "I"
        facturamaFactura.setPaymentForm(factura.getFormaDePago().getClaveSat()); // Usar el atributo formaDePago.claveSat de la entidad Factura
        facturamaFactura.setPaymentMethod("PUE"); // Valor fijo "PUE"

        // Crear objeto Receiver y establecer valores
        Receiver receiver = new Receiver();
        receiver.setRfc(factura.getReceptor().getRfc()); // Usar el atributo receptor.rfc de la entidad Factura
        receiver.setName(factura.getReceptor().getRazonSocial()); // Usar el atributo receptor.razonSocial de la entidad Factura
        receiver.setCfdiUse(factura.getUsoDeCfdi().getClave()); // Usar el atributo usoDeCfdi.clave de la entidad Factura
        facturamaFactura.setReceiver(receiver);

        // Crear lista de Items y establecer valores
        List<Item> items = new ArrayList<>();
        for (ConceptoDePago concepto : factura.getConceptos()) {
            Item item = new Item();
            item.setProductCode(concepto.getClaveSat().toString()); // Usar el atributo claveSat de la entidad ConceptoDePago
            item.setIdentificationNumber(concepto.getClaveComercial().toString()); // Usar el atributo descripcion de la entidad ConceptoDePago
            item.setDescription(concepto.getDescripcion()); // Usar el atributo descripcion de la entidad ConceptoDePago
            item.setUnit("Unidad de Servicio"); // Valor fijo "NO APLICA"
            item.setUnitCode("E48"); // No hay un atributo correspondiente en la entidad Factura, dejar vacío
            item.setUnitPrice(concepto.getMonto()); // Usar el atributo monto de la entidad ConceptoDePago
            item.setQuantity(1.0); // Valor fijo 1.0
            item.setSubtotal(concepto.getMonto()); // Usar el atributo monto de la entidad ConceptoDePago
            item.setDiscount(0.0); // Valor fijo 0.0

            List<Tax> taxes = new ArrayList<>();
            Tax tax = new Tax();
            tax.setTotal(concepto.getMonto() * concepto.getTasa()/100); // Calcular el total del impuesto
            tax.setName("IVA"); // Valor fijo "IVA"
            tax.setBase(concepto.getMonto()); // Usar el atributo monto de la entidad ConceptoDePago
            tax.setRate(concepto.getTasa()/100); // tasa
            tax.setIsRetention(false); // Valor fijo false

            taxes.add(tax);
            item.setTaxes(taxes);

            item.setTotal(concepto.getMonto() * (1+concepto.getTasa()/100)); // Calcular el total (monto + impuesto)

            items.add(item);
        }

        facturamaFactura.setItems(items);
        return  facturamaFactura;
    }

    public <T> T jsonAModelo(String jsonString, Class<T> clase)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, clase);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
