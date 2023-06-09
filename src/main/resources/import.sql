INSERT INTO `facturacion`.cat_regimen_fiscal ( id, clave, descripcion ) VALUES ( 1, '10', 'PERSONA FISICA CON ACTIVIDADES EMPRESARIALES' );
INSERT INTO `facturacion`.cat_regimen_fiscal ( id, clave, descripcion ) VALUES ( 2, '20', 'REGIMEN SIMPLIFICADO DE CONFIANZA' );

INSERT INTO `facturacion`.`cat_receptores` (`id`, `codigoPostal`, `email`, `razonSocial`, `rfc`, `cat_regimen_fiscal_id`) VALUES 	(1, 33800, 'jesusmanuelgalindo@gmail.com', 'INDUSTRIAS EL GALINDO', 'GAAJ881104IU0', 1);

INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('EF', '01', 'Efectivo');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('CH', '02', 'Cheque nominativo');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('TR', '03', 'Transferencia electrónica de fondos');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('TC', '04', 'Tarjeta de crédito');
INSERT INTO `facturacion`.`cat_formas_de_pago` (`claveComercial`, `claveSat`, `descripcion`) VALUES ('TD', '28', 'Tarjeta de débito');
