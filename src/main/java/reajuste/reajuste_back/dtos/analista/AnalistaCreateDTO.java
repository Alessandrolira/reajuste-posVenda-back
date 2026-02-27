package reajuste.reajuste_back.dtos.analista;

import jakarta.validation.constraints.NotBlank;

public record AnalistaCreateDTO(

        @NotBlank(message = "O nome não pode ser nulo")
        String nome

) {
}
