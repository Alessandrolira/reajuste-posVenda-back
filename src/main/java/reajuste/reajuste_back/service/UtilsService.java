package reajuste.reajuste_back.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class UtilsService {

    public BigDecimal calcularReajuste(@NotBlank BigDecimal valorFatura, @NotNull BigDecimal porcentagem) {

        return valorFatura
                .multiply(
                        BigDecimal.ONE.add(
                                porcentagem.divide(
                                        BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP)
                        )
                )
                .setScale(2, RoundingMode.HALF_UP);

    }

    public BigDecimal calcularEconomiaReal(Negociacao negociacao, Reajuste reajuste) {

        if (negociacao.getStatus() == EnumStatusNegociacao.EM_ANDAMENTO){
            return null;
        }

        return BigDecimal.valueOf(
                calcularReajuste(
                        reajuste.getValorUltimaFatura(),
                        negociacao.getPorcentagemPropostaOperadora()
                ).doubleValue() -  calcularReajuste(
                        reajuste.getValorUltimaFatura(),
                        negociacao.getPorcentagemFechada()
                ).doubleValue());

    }

    public BigDecimal calcularDiferencaPercentual(Negociacao negociacao) {

        return BigDecimal.valueOf(
                negociacao.getPorcentagemPropostaOperadora().doubleValue()
                        - negociacao.getPorcentagemFechada().doubleValue());

    }

}
