package reajuste.reajuste_back.dtos.negociacao;

import lombok.Builder;
import reajuste.reajuste_back.dtos.interacao.InteracoesDTO;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
public record NegociacaoDTO(

        Integer idNegociacao,
        Integer idReajuste,
        EnumStatusNegociacao status,
        LocalDate dtInicio,
        LocalDate dtFim,
        String motivoEncerramento,
        BigDecimal porcentagemFechada,
        BigDecimal porcentagemOperadora,
        BigDecimal valorFinal,
        BigDecimal valorInicial,
        BigDecimal valorComPrimeiraPorcentagem,
        List<InteracoesDTO> interacoes

) {
}
