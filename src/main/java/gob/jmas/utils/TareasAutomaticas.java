package gob.jmas.utils;

import gob.jmas.model.facturacion.Factura;
import gob.jmas.model.facturacion.FormaDePago;
import gob.jmas.service.factura.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;


