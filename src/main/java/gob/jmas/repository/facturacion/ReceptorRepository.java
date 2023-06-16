package gob.jmas.repository.facturacion;

import gob.jmas.model.facturacion.Receptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ReceptorRepository extends JpaRepository<Receptor, Integer> {

    Optional<Receptor> findByRfc(String rfc);

    Optional<Receptor> findByRazonSocial(String razonSocial);

}