package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record EmpresaMelhorNegociacaoDTO(

        String nomeEmpresa,
        double valorEconomizado

) {
}
