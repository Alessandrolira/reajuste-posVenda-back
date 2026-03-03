package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;
import reajuste.reajuste_back.enums.empresa.EnumModalidade;
import reajuste.reajuste_back.enums.empresa.EnumOperadora;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record CardsEmpresaDTO(

        Integer idEmpresa,
        String nomeEmpresa,
        EnumOperadora operadora,
        EnumModalidade modalidade,
        EnumStatusRenovacao statusRenovacao,
        LocalDate aniversario,
        int ultimoReajuste,
        BigDecimal economiaTotal,
        BigDecimal porcentagemUltimoReajuste

) {
}
