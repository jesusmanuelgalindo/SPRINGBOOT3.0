package gob.jmas.repository.facturacion;

import gob.jmas.model.facturacion.UsoDeCfdi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsoDeCfdiRepository extends JpaRepository<UsoDeCfdi, Integer> {

}
