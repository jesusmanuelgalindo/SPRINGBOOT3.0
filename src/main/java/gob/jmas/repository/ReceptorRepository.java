package gob.jmas.repository;

import gob.jmas.model.Receptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ReceptorRepository extends JpaRepository<Receptor, Integer> {

    Optional<Receptor> findByRfc(String rfc);

}