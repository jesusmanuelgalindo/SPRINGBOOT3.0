package gob.jmas.repository.facturacion;

import gob.jmas.model.facturacion.Receptor;
import gob.jmas.model.facturacion.RegimenFiscal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegimenFiscalRepository extends JpaRepository<RegimenFiscal, Integer> {


}
