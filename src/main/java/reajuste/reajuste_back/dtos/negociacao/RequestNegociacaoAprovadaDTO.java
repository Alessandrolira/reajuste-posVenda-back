package reajuste.reajuste_back.dtos.negociacao;

import jakarta.validation.constraints.NotNull;

public record RequestNegociacaoAprovadaDTO(

        @NotNull
        Integer idNegociacao

) {
}
