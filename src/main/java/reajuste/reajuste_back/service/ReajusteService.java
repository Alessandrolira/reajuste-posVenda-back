package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.reajuste.ReajusteCreateDTO;
import reajuste.reajuste_back.dtos.reajuste.ReajusteResponseDTO;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;
import reajuste.reajuste_back.enums.reajuste.EnumStatusReajuste;
import reajuste.reajuste_back.repository.EmpresaRepository;
import reajuste.reajuste_back.repository.ReajusteRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class ReajusteService {

    private final ReajusteRepository reajusteRepository;
    private final EmpresaRepository empresaRepository;
    private final NegociacaoService negociacaoService;

    public ReajusteResponseDTO criarReajuste(@Valid ReajusteCreateDTO body) throws Exception {

        Empresa empresaEncontrada = empresaRepository.findById(body.idEmpresa()).orElseThrow(() -> new Exception("Erro ao encontrar a empresa"));

        if (reajusteRepository.existsByEmpresaAndAnoReferencia(empresaEncontrada, body.anoReferencia())) {
            throw new RuntimeException("Já existe reajuste para essa empresa nesse ano");
        }

        BigDecimal calculo = calcularReajuste(body.valorUltimaFatura(), body.porcentagemOperadora());

        Reajuste reajuste = new Reajuste();

        reajuste.setEmpresa(empresaEncontrada);
        reajuste.setAnoReferencia(body.anoReferencia());
        reajuste.setValorUltimaFatura(body.valorUltimaFatura());
        reajuste.setPorcentagemOperadora(body.porcentagemOperadora());
        reajuste.setVlComPrimeiraPorcentagem(calculo);
        reajuste.setDtEnvio(body.dataEnvioProposta());

        Reajuste objetoReajuste = reajusteRepository.save(reajuste);

        empresaEncontrada.setStatusRenovacao(EnumStatusRenovacao.EM_NEGOCIACAO);

        empresaRepository.save(empresaEncontrada);

        return ReajusteResponseDTO.builder()
                .idReajuste(objetoReajuste.getIdReajuste())
                .idEmpresa(objetoReajuste.getEmpresa().getIdEmpresa())
                .anoReferencia(objetoReajuste.getAnoReferencia())
                .valorUltimaFatura(objetoReajuste.getValorUltimaFatura())
                .porcentagemOferencidaOperadora(objetoReajuste.getPorcentagemOperadora())
                .valorComPrimeiraPorcentagem(objetoReajuste.getVlComPrimeiraPorcentagem())
                .dataEnvio(objetoReajuste.getDtEnvio())
                .build();

    }

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

    public Reajuste buscarUltimoReajuste(Empresa empresa) {

        if (reajusteRepository
                .findTopByEmpresaOrderByAnoReferenciaDesc(empresa) == null){
            return null;
        } else {
            return reajusteRepository
                    .findTopByEmpresaOrderByAnoReferenciaDesc(empresa);
        }




    }

    public BigDecimal calcularEconomiaReal(Negociacao negociacao, Reajuste reajuste) {

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
