package reajuste.reajuste_back.dtos.interacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RequestInteracaoAprovadaDTO(

        @NotNull
        Integer idInteracao,

        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataAceite,

        @NotNull
        String motivoEncerramento

) {
}
