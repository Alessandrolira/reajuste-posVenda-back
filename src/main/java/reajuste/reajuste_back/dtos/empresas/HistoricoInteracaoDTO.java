package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;
import reajuste.reajuste_back.enums.interacao.EnumTipoInteracao;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record HistoricoInteracaoDTO(

        Integer id,
        Integer ano,
        EnumTipoInteracao tipo,
        BigDecimal porcentagemProposta,
        BigDecimal valorAtual,
        BigDecimal vlMensalResultante,
        LocalDate dtInteracao,
        String observacao,
        Boolean isAceita

) {
}
