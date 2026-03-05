package reajuste.reajuste_back.repository;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.util.List;

public interface ReajusteRepository extends JpaRepository<Reajuste, Integer> {

    boolean existsByEmpresaAndAnoReferencia(Empresa empresaEncontrada, @NotBlank Integer s);

    Reajuste findTopByEmpresaOrderByAnoReferenciaDesc(Empresa empresa);

    List<Reajuste> findAllByEmpresa(Empresa empresa);

    boolean existsByEmpresa(Empresa empresa);

    Reajuste findTopByEmpresaAndNegociacaoStatusOrderByAnoReferenciaDesc(
            Empresa empresa,
            EnumStatusNegociacao status
    );
}
