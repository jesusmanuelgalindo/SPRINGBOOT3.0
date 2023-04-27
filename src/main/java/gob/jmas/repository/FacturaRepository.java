package gob.jmas.repository;

import gob.jmas.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {

        Optional<Factura> findById(Integer id);

      //    @Modifying
      //    @Query("UPDATE Product p SET p.price = :newPrice WHERE p.price < :oldPrice")
      //    int updatePrices(Double oldPrice, Double newPrice);


}