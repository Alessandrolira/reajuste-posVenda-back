package reajuste.reajuste_back.dtos.analista;

import lombok.Builder;

@Builder
public record AnalistasDTO(

        Integer id,
        String nome

) {
}
