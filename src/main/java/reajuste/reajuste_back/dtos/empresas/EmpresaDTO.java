package reajuste.reajuste_back.dtos.empresas;

import lombok.Builder;

import reajuste.reajuste_back.enums.empresa.EnumModalidade;
import reajuste.reajuste_back.enums.empresa.EnumOperadora;

import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;


@Builder
public record EmpresaDTO (

        String nomeEmpresa,
        EnumStatusRenovacao statusRenovacao,
        EnumOperadora operadora,
        EnumModalidade modalidade


){


}
