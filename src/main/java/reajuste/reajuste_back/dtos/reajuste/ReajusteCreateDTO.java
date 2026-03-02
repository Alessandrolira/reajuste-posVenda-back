package reajuste.reajuste_back.dtos.reajuste;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import reajuste.reajuste_back.enums.reajuste.EnumStatusReajuste;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record ReajusteCreateDTO(

        @NotNull
        Integer idEmpresa,

        @NotNull
        Integer anoReferencia,

        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dataEnvioProposta,

        @NotNull
        BigDecimal porcentagemOperadora,

        @NotNull
        BigDecimal valorUltimaFatura


) {
}
