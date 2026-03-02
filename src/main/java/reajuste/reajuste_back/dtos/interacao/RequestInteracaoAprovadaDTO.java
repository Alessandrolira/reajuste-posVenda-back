package reajuste.reajuste_back.dtos.interacao;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RequestInteracaoAprovadaDTO(

        @NotNull
        Integer idInteracao

) {
}
