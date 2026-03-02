package reajuste.reajuste_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.entity.Negociacao;

public interface NegociacaoRepository extends JpaRepository<Negociacao, Integer> {
}
