package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record LinhaTempoDTO(

        Integer ano,
        BigDecimal porcentagemFechada,
        BigDecimal economiaPorcentagem

) {
}
