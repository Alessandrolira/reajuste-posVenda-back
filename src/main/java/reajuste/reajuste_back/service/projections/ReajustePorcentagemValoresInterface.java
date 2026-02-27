package reajuste.reajuste_back.service.projections;

import java.math.BigDecimal;

public interface ReajustePorcentagemValoresInterface {

    Integer getIdReajuste();
    BigDecimal getValorUltimaFatura();
    BigDecimal getPorcentagemOperadora();

}
