package gob.jmas.repository.comercial;


import gob.jmas.model.comercial.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

       @Query(value = "select pagos.Pagos_Id as id,usua_pa as cuenta,(select nomb_us from usuarios where clav_us =usua_pa ) as nombre," +
               "(select dire_us from usuarios where clav_us =usua_pa ) as direccion, caja_pa as caja,refe_pa as referencia,fopa_pa as formaDePago," +
               " date(fech_pa) as fechaDePago ,conc_pa as claveComercial,(select conceptos.c_claveProvServ from conceptos where clav_cc=conc_pa) as claveSat," +
               "(select desc_cc from conceptos where clav_cc=conc_pa) as descripcion, mont_pa as monto, tasa_pa as tasa from pagos where" +
               " caja_pa=:caja and refe_pa=:referencia and usua_pa=:cuenta" +
               " GROUP BY conc_pa", nativeQuery = true)
       List<Pago> consultarDetalleDePago(String cuenta, Integer caja, Integer referencia);

}
