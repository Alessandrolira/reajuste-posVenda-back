package reajuste.reajuste_back.dtos.negociacao;

import lombok.Builder;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.time.LocalDate;

@Builder
public record NegociacaoResponseDTO(

        Negociacao Negociacao,
        Reajuste Reajuste,
        EnumStatusNegociacao status,
        LocalDate dtInicio,
        LocalDate dtFim,
        String motivoEncerramento

) {
}
