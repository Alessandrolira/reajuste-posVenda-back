package reajuste.reajuste_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.service.projections.ReajustePorcentagemValoresInterface;

import java.util.List;

public interface ReajusteRepository extends JpaRepository<Reajuste, Integer> {

    List<ReajustePorcentagemValoresInterface> findAllBy();
}
