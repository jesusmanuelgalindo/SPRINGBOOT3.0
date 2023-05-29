INSERT INTO `facturacion`.cat_regimen_fiscal ( id, clave, descripcion ) VALUES ( 1, '10', 'PERSONA FISICA CON ACTIVIDADES EMPRESARIALES' );
INSERT INTO `facturacion`.cat_regimen_fiscal ( id, clave, descripcion ) VALUES ( 2, '20', 'REGIMEN SIMPLIFICADO DE CONFIANZA' );

INSERT INTO `facturacion`.`cat_receptores` (`id`, `codigoPostal`, `email`, `razonSocial`, `rfc`, `cat_regimen_fiscal_id`) VALUES 	(1, 33800, 'jesusmanuelgalindo@gmail.com', 'INDUSTRIAS EL GALINDO', 'GAAJ881104IU0', 1);