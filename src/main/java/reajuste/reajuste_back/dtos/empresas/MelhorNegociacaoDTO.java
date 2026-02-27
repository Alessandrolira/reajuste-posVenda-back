package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record MelhorNegociacaoDTO(

        String nomeEmpresa,
        BigDecimal valorReducao

) {
}
