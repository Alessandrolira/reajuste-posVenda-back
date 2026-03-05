package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PorcentagensFinaisIniciaisDTO(

        BigDecimal operadora,
        BigDecimal corretora,
        Integer ano

) {
}
