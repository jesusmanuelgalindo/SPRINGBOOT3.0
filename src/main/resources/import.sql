INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (601, 'General de Ley Personas Morales');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (603, 'Personas Morales con Fines no Lucrativos');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (605, 'Sueldos y Salarios e Ingresos Asimilados a Salarios');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (606, 'Arrendamiento');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (607, 'Régimen de Enajenación o Adquisición de Bienes');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (608, 'Demás ingresos');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (610, 'Residentes en el Extranjero sin Establecimiento Permanente en México');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (611, 'Ingresos por Dividendos (socios y accionistas)');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (612, 'Personas Físicas con Actividades Empresariales y Profesionales');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (614, 'Ingresos por intereses');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (615, 'Régimen de los ingresos por obtención de premios');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (616, 'Sin obligaciones fiscales');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (620, 'Sociedades Cooperativas de Producción que optan por diferir sus ingresos');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (621, 'Incorporación Fiscal');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (622, 'Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (623, 'Opcional para Grupos de Sociedades');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (624, 'Coordinados');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (625, 'Régimen de las Actividades Empresariales con ingresos a través de Plataformas Tecnológicas');
INSERT INTO `facturacion`.`cat_regimen_fiscal` (`clave`, `descripcion`) VALUES (626, 'Régimen Simplificado de Confianza');

INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('EF', '01', 'Efectivo');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('CH', '02', 'Cheque nominativo');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('TR', '03', 'Transferencia electrónica de fondos');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('TC', '04', 'Tarjeta de crédito');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('TD', '28', 'Tarjeta de débito');

INSERT INTO `facturacion`.`cat_usos_de_cfdi` (`clave`, `descripcion`) VALUES ('G01', 'Adquisición de mercancías.');
INSERT INTO `facturacion`.`cat_usos_de_cfdi` (`clave`, `descripcion`) VALUES ('G03', 'Gastos en general.');
INSERT INTO `facturacion`.`cat_usos_de_cfdi` (`clave`, `descripcion`) VALUES ('I01', 'Construcciones.');
INSERT INTO `facturacion`.`cat_usos_de_cfdi` (`clave`, `descripcion`) VALUES ('I05', 'Dados, troqueles, moldes, matrices y herramental.');
INSERT INTO `facturacion`.`cat_usos_de_cfdi` (`clave`, `descripcion`) VALUES ('I08', 'Otra maquinaria y equipo.');
INSERT INTO `facturacion`.`cat_usos_de_cfdi` (`clave`, `descripcion`) VALUES ('S01', 'Sin efectos fiscales.  ');

INSERT INTO `facturacion`.`cat_receptores` (`id`, `codigoPostal`, `email`, `razonSocial`, `rfc`, `cat_regimen_fiscal_id`) VALUES 	(1, 33800, 'jesusmanuelgalindo@gmail.com', 'INDUSTRIAS EL GALINDO', 'GAAJ881104IU0', 1);
