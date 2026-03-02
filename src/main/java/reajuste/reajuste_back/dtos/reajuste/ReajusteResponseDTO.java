package reajuste.reajuste_back.dtos.reajuste;

import lombok.Builder;
import reajuste.reajuste_back.entity.Empresa;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ReajusteResponseDTO(

       Integer idReajuste,
       Integer idEmpresa,
       Integer anoReferencia,
       BigDecimal valorUltimaFatura,
       BigDecimal porcentagemOferencidaOperadora,
       BigDecimal valorComPrimeiraPorcentagem,
       LocalDate dataEnvio


) {
}
