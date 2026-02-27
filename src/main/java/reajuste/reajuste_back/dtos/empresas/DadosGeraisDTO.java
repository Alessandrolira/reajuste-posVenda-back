package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record DadosGeraisDTO(

        Long totalEmpresas,
        BigDecimal economiaTotal,
        BigDecimal mediaReducao,
        MelhorNegociacaoDTO melhorNegociacao

) {
}
