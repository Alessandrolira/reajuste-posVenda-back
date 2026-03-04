package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;
import lombok.Data;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record EmpresaDTO (
        String nomeEmpresa,
        EnumStatusRenovacao statusRenovacao,
        BigDecimal ultimoReajusteOferecido,
        BigDecimal ultimoReajusteFechado,
        BigDecimal economiaPercentual,
        BigDecimal economiaReal,
        BigDecimal valorUltimaFatura,
        BigDecimal valorFechado,
        List<PorcentagensFinaisIniciaisDTO> porcentagensFinaisIniciais,
        List<LinhaTempoDTO> linhaTempo,
        List<HistoricoInteracaoDTO> historicoInteracao

){


}
