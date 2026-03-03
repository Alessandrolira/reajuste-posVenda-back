package reajuste.reajuste_back.dtos.interacao;

import lombok.Builder;
import reajuste.reajuste_back.enums.interacao.EnumTipoInteracao;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record InteracoesDTO(

    Integer idInteracao,
    Integer idNegociacao,
    EnumTipoInteracao tipo,
    BigDecimal porcentagemProposta,
    BigDecimal vlMensalResultante,
    String observacao,
    LocalDate dtInteracao,
    Boolean isAceita

) {
}
