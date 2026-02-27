package reajuste.reajuste_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Analista;

public interface AnalistaRepository extends JpaRepository<Analista, Integer> {
}
