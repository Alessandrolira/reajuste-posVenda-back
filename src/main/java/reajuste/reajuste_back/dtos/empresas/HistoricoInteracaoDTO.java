package reajuste.reajuste_back.dtos.empresas;

import com.fasterxml.jackson.annotation.JsonFormat;
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

        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dtInteracao,
        String observacao,
        Boolean isAceita

) {
}
