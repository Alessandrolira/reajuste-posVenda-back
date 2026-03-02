package reajuste.reajuste_back.dtos.negociacao;

import lombok.Builder;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ResponseNegociacaoAprovadaDTO(

        Integer idNegociacao,
        Integer idReajuste,
        EnumStatusNegociacao status,
        LocalDate dtInicio,
        LocalDate dtFim,
        String motivoEncerramento,
        BigDecimal porcentagemFechada,
        BigDecimal porcentagemOperadora,
        BigDecimal valorFinal,
        BigDecimal valorInicial

) {
}
