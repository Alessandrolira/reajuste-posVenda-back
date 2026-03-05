package reajuste.reajuste_back.dtos.reajuste;

import lombok.Builder;
import reajuste.reajuste_back.dtos.empresas.HistoricoInteracaoDTO;
import reajuste.reajuste_back.dtos.empresas.PorcentagensFinaisIniciaisDTO;
import reajuste.reajuste_back.dtos.interacao.InteracoesDTO;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record UltimoReajusteDTO(

        Integer idReajuste,
        EnumStatusNegociacao statusNegociacao,
        Integer anoUltimoReajuste,
        BigDecimal porcentagemOferecida,
        BigDecimal porcentagemFechada,
        BigDecimal economiaPercentual,
        BigDecimal economiaReal,
        BigDecimal valorPrimeiraFatura,
        BigDecimal mediaReducao,
        List<PorcentagensFinaisIniciaisDTO> porcentagensFinaisIniciais,
        List<HistoricoInteracaoDTO> historicoInteracoes

) {
}
