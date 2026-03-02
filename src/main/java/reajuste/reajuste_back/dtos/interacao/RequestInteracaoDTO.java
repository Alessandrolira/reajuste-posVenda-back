package reajuste.reajuste_back.dtos.interacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import reajuste.reajuste_back.enums.interacao.EnumTipoInteracao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RequestInteracaoDTO(

        @NotNull
        Integer idReajuste,

        @NotNull
        EnumTipoInteracao solicitante,

        @NotNull
        BigDecimal proposta,

        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataProposta,

        @NotBlank
        String observacoes


) {
}
