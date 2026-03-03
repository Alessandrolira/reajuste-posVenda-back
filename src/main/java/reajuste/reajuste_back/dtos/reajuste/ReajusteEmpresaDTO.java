package reajuste.reajuste_back.dtos.reajuste;

import lombok.Builder;
import reajuste.reajuste_back.dtos.negociacao.NegociacaoDTO;
import reajuste.reajuste_back.entity.Negociacao;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ReajusteEmpresaDTO(

        Integer idReajuste,
        Integer idEmpresa,
        String anoReferencia,
        BigDecimal vlUltimaFatura,
        BigDecimal porcentagemOferecidaOperadora,
        BigDecimal vlComPrimeiraPorcentagem,
        LocalDate dtEnvio,
        NegociacaoDTO negociacao

) {
}
