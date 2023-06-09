package gob.jmas.repository.facturacion;

import gob.jmas.model.facturacion.FormaDePago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface FormaDePagoRepository extends JpaRepository<FormaDePago, Integer> {

    Optional<FormaDePago> findByClaveComercial(String claveComercial);

}