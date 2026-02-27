package reajuste.reajuste_back.dtos.empresas;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import reajuste.reajuste_back.enums.empresa.*;

import java.time.LocalDate;

public record CriarEmpresaDTO(

        @NotBlank
        String nomeEmpresa,

        @NotNull
        Integer idAnalista,

        @NotNull
        EnumOperadora operadora,

        @NotNull
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate aniversario,

        @NotNull
        EnumModalidade modalidade,

        @NotNull
        EnumStatusContrato statusContrato,

        @NotNull
        EnumPorte porte

    ) {
}
