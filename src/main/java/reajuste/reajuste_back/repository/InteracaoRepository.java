package reajuste.reajuste_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Interacao;
import reajuste.reajuste_back.entity.Negociacao;

import java.util.List;

public interface InteracaoRepository extends JpaRepository<Interacao, Integer> {

    boolean existsByNegociacao_IdNegociacaoAndAceitaTrue(Integer idNegociacao);

    List<Interacao> findAllByNegociacao(Negociacao negociacao);
}
