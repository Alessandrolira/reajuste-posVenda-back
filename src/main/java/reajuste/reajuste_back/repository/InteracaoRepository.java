package reajuste.reajuste_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Interacao;

public interface InteracaoRepository extends JpaRepository<Interacao, Integer> {

    boolean existsByNegociacao_IdNegociacaoAndAceitaTrue(Integer idNegociacao);
}
