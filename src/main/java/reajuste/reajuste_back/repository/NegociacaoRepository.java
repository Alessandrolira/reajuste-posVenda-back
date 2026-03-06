package reajuste.reajuste_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import reajuste.reajuste_back.dtos.negociacao.NegociacaoResponseDTO;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;

import java.util.List;

public interface NegociacaoRepository extends JpaRepository<Negociacao, Integer> {


    Negociacao findByReajuste_IdReajuste(Integer idReajuste);

    Boolean existsByReajuste_IdReajuste(Integer idReajuste);

    Negociacao findAllByReajuste(Reajuste reajuste);

    boolean existsByReajuste(Reajuste ultimoReajuste);

    Negociacao findByReajusteEmpresa(Empresa empresa);
}
