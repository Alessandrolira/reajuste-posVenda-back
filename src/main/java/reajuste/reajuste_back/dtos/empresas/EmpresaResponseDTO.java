package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;
import reajuste.reajuste_back.dtos.reajuste.ReajusteEmpresaDTO;
import reajuste.reajuste_back.entity.Analista;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.empresa.EnumOperadora;
import reajuste.reajuste_back.enums.empresa.EnumPorte;
import reajuste.reajuste_back.enums.empresa.EnumStatusContrato;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EmpresaResponseDTO(

        Integer idEmpresa,
        String nomeEmpresa,
        EnumStatusContrato statusContrato,
        EnumOperadora operadora,
        LocalDate dtAniversario,
        EnumPorte porte,
        EnumStatusRenovacao statusRenovacao,
        Analista analista,
        List<ReajusteEmpresaDTO> Reajuste

) {

}
