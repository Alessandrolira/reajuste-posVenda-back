package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.empresas.HistoricoInteracaoDTO;
import reajuste.reajuste_back.dtos.reajuste.ReajusteCreateDTO;
import reajuste.reajuste_back.dtos.reajuste.ReajusteResponseDTO;
import reajuste.reajuste_back.dtos.reajuste.UltimoReajusteDTO;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Interacao;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;
import reajuste.reajuste_back.repository.EmpresaRepository;
import reajuste.reajuste_back.repository.InteracaoRepository;
import reajuste.reajuste_back.repository.NegociacaoRepository;
import reajuste.reajuste_back.repository.ReajusteRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ReajusteService {

    private final ReajusteRepository reajusteRepository;
    private final EmpresaRepository empresaRepository;
    private final NegociacaoService negociacaoService;
    private final NegociacaoRepository negociacaoRepository;
    private final InteracaoRepository interacaoRepository;

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

        negociacaoService.criarNegociacao(objetoReajuste);

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

    public UltimoReajusteDTO buscarUltimoReajusteAprovado(Integer idEmpresa) {


        Empresa empresa = empresaRepository.findById(idEmpresa).orElseThrow();

        Reajuste reajuste = reajusteRepository.findTopByEmpresaAndNegociacaoStatusOrderByAnoReferenciaDesc(empresa, EnumStatusNegociacao.FINALIZADA);

        if (reajuste == null){
            return null;
        }

        Negociacao negociacaoVinculada = negociacaoRepository.findAllByReajuste(reajuste);

        return UltimoReajusteDTO.builder()
                .idReajuste(reajuste.getIdReajuste())
                .statusNegociacao(negociacaoVinculada.getStatus())
                .anoUltimoReajuste(reajuste.getAnoReferencia())
                .porcentagemOferecida(negociacaoVinculada.getPorcentagemPropostaOperadora())
                .porcentagemFechada(negociacaoVinculada.getPorcentagemFechada())
                .economiaPercentual(calcularDiferencaPercentual(negociacaoVinculada))
                .economiaReal(calcularEconomiaReal(negociacaoVinculada, reajuste))
                .valorPrimeiraFatura(negociacaoVinculada.getValorInicial())
                .mediaReducao(negociacaoService.buscarMedia(empresa))
                .porcentagensFinaisIniciais(negociacaoService.buscarPorcentagensFinaisIniciais(empresa))
                .historicoInteracoes(buscarInteracoes(empresa))
                .build();

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

    public List<HistoricoInteracaoDTO> buscarInteracoes(Empresa empresa) {

        List<Reajuste> reajustes = reajusteRepository.findAllByEmpresa(empresa);
        List<Negociacao> negociacoes = new ArrayList<>();

        for (Reajuste reajuste : reajustes) {

            Negociacao negociacao = negociacaoRepository.findAllByReajuste(reajuste);
            negociacoes.add(negociacao);

        }

        List<HistoricoInteracaoDTO> interacoes = new ArrayList<>();

        for (Negociacao negociacao : negociacoes){

            List<Interacao> interacoesObtidas = interacaoRepository.findAllByNegociacao(negociacao);

            for (Interacao interacao : interacoesObtidas){

                HistoricoInteracaoDTO interacaoDTO = HistoricoInteracaoDTO.builder()
                        .id(interacao.getIdInteracao())
                        .ano(negociacao.getReajuste().getAnoReferencia())
                        .tipo(interacao.getTipoInteracao())
                        .porcentagemProposta(interacao.getPorcentagemProposta())
                        .valorAtual(negociacao.getValorInicial())
                        .vlMensalResultante(calcularReajuste(
                                negociacao.getValorInicial(),
                                interacao.getPorcentagemProposta()
                        ))
                        .dtInteracao(interacao.getDtInteracao())
                        .observacao(interacao.getObservacao())
                        .isAceita(interacao.getAceita())
                        .build();

                interacoes.add(interacaoDTO);

            }
        }

        return interacoes;

    }
}
