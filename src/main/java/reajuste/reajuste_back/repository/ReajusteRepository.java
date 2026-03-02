package reajuste.reajuste_back.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.service.projections.ReajustePorcentagemValoresInterface;

import java.util.List;

public interface ReajusteRepository extends JpaRepository<Reajuste, Integer> {

    List<ReajustePorcentagemValoresInterface> findAllBy();

    boolean existsByEmpresaAndAnoReferencia(Empresa empresaEncontrada, @NotBlank Integer s);
}
