package reajuste.reajuste_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
}
