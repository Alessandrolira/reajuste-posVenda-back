package reajuste.reajuste_back.dtos.negociacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import reajuste.reajuste_back.dtos.empresas.HistoricoInteracaoDTO;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record NegociacaoEmAbertoDTO(

        Integer idReajuse,
        EnumStatusNegociacao statusNegociacao,

        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate dtAbertura,

        BigDecimal propostaInicial,
        BigDecimal valorAtual,
        BigDecimal valorAposReajuste,
        BigDecimal aumentoDe,
        String observacaoReajuste,
        List<HistoricoInteracaoDTO> historicoInteracao

) {
}
