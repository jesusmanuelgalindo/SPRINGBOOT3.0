package gob.jmas.repository.facturacion;

import gob.jmas.model.comercial.Pago;
import gob.jmas.model.facturacion.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

        Optional<Factura> findById(Integer id);

      //    @Modifying
      //    @Query("UPDATE Product p SET p.price = :newPrice WHERE p.price < :oldPrice")
      //    int updatePrices(Double oldPrice, Double newPrice);

        @Query(value = "select * from factura where cuenta=:cuenta and caja=:caja and referencia=:referencia and activa=1 limit 1", nativeQuery = true)
        Optional<Factura> findFacturaByCuentaCajaReferencia(String cuenta, Integer caja, Integer referencia);
}