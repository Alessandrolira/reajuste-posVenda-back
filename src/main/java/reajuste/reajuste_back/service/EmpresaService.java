package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.empresas.*;
import reajuste.reajuste_back.dtos.reajuste.UltimoReajusteDTO;
import reajuste.reajuste_back.entity.*;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;
import reajuste.reajuste_back.repository.AnalistaRepository;
import reajuste.reajuste_back.repository.EmpresaRepository;
import reajuste.reajuste_back.repository.NegociacaoRepository;
import reajuste.reajuste_back.repository.ReajusteRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AnalistaRepository analistaRepository;
    private final NegociacaoRepository negociacaoRepository;
    private final ReajusteService reajusteService;
    private final NegociacaoService negociacaoService;
    private final InteracaoService interacaoService;
    private final ReajusteRepository reajusteRepository;

    public Boolean criarEmpresa(@Valid CriarEmpresaDTO body) throws Exception {


        try {
            Analista analistaEncontrado = analistaRepository.findById(body.idAnalista()).orElseThrow(() -> new Exception("Erro ao encontraro analista"));

            EnumStatusRenovacao statusRenovacao = verificarStatusRenovacao(body.aniversario());

            Empresa empresa = new Empresa();

            empresa.setNome(body.nomeEmpresa());
            empresa.setStatusContrato(body.statusContrato());
            empresa.setOperadora(body.operadora());
            empresa.setDtAniversario(body.aniversario());
            empresa.setModalidade(body.modalidade());
            empresa.setPorte(body.porte());
            empresa.setStatusRenovacao(statusRenovacao);
            empresa.setIdAnalista(analistaEncontrado);

            empresaRepository.save(empresa);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar a Empresa " + e);
            return false;
        }


    }

    private EnumStatusRenovacao verificarStatusRenovacao(LocalDate aniversarioOriginal) {

        LocalDate hoje = LocalDate.now();

        // Cria aniversário no ano atual
        LocalDate aniversarioEsteAno = aniversarioOriginal.withYear(hoje.getYear());

        if (aniversarioEsteAno.isBefore(hoje)) {
            aniversarioEsteAno = aniversarioEsteAno.plusYears(1);
        }

        long dias = ChronoUnit.DAYS.between(hoje, aniversarioEsteAno);

        if (dias <= 90) {
            return EnumStatusRenovacao.PENDENTE;
        } else {
            return EnumStatusRenovacao.REAJUSTADO;
        }
    }

    public Long totalEmpresas() {

        return empresaRepository.count();

    }

    public BigDecimal economiaTotal() {

        List<Negociacao> negociacoes = negociacaoRepository.findAll();
        double economia = 0;

        for (Negociacao negociacao: negociacoes){
            if (negociacao.getValorComPrimeiraPorcentagem() == null || negociacao.getValorFinal() == null){
                continue;
            }
            System.out.println(negociacao.getValorComPrimeiraPorcentagem().doubleValue() + "  " + negociacao.getValorFinal().doubleValue());
            economia += (
                    negociacao.getValorComPrimeiraPorcentagem().doubleValue()
                            - negociacao.getValorFinal().doubleValue()
                    );
        }

        return BigDecimal.valueOf(economia);
    }

    public BigDecimal mediaReducao() {

        List<Negociacao> negociaoes = negociacaoRepository.findAll();
        double soma = 0;
        double contagemElementos = 0;

        for (Negociacao negociacao : negociaoes){
            if (negociacao.getPorcentagemPropostaOperadora() == null || negociacao.getPorcentagemFechada() == null){
                continue;
            }
            contagemElementos += 1;
            soma += (negociacao.getPorcentagemPropostaOperadora().doubleValue() - negociacao.getPorcentagemFechada().doubleValue());
        }

        if (contagemElementos == 0){
            return BigDecimal.valueOf(0);
        } else {
            return BigDecimal.valueOf(soma/contagemElementos);
        }



    }

    public EmpresaMelhorNegociacaoDTO encontrarMelhorNegociacao() {

        List<Negociacao> negociacoes = negociacaoRepository.findAll();
        double valorMelhorNegociacao = 0;
        String nomeEmpresa = "";

        for (Negociacao negociacao : negociacoes){
            if (negociacao.getValorComPrimeiraPorcentagem() == null || negociacao.getValorFinal() == null){
                continue;
            }

            if (negociacao.getValorComPrimeiraPorcentagem().doubleValue() - negociacao.getValorFinal().doubleValue() > valorMelhorNegociacao){
                valorMelhorNegociacao = negociacao.getValorComPrimeiraPorcentagem().doubleValue() - negociacao.getValorFinal().doubleValue();
                nomeEmpresa = negociacao.getReajuste().getEmpresa().getNome();
            }
        }

        return EmpresaMelhorNegociacaoDTO.builder()
                .nomeEmpresa(nomeEmpresa)
                .valorEconomizado(valorMelhorNegociacao)
                .build();

    }

//    public List<CardsEmpresaDTO> gerarCardsEmpresas() {
//
//        List<Empresa> empresas = empresaRepository.findAll();
//        List<CardsEmpresaDTO> empresasBuscadas = new ArrayList<>();
//
//        for (Empresa empresa : empresas){
//
//            if(reajusteService.buscarUltimoReajusteAprovado(empresa.getIdEmpresa()) == null){
//                CardsEmpresaDTO card = CardsEmpresaDTO.builder()
//                        .idEmpresa(empresa.getIdEmpresa())
//                        .nomeEmpresa(empresa.getNome())
//                        .operadora(empresa.getOperadora())
//                        .modalidade(empresa.getModalidade())
//                        .statusRenovacao(empresa.getStatusRenovacao())
//                        .aniversario(empresa.getDtAniversario())
//                        .ultimoReajuste(0)
//                        .economiaTotal(BigDecimal.valueOf(0))
//                        .porcentagemUltimoReajuste(BigDecimal.valueOf(0))
//                        .build();
//
//                empresasBuscadas.add(card);
//
//                continue;
//            }
//
//            UltimoReajusteDTO ultimoReajuste = reajusteService.buscarUltimoReajusteAprovado(empresa.getIdEmpresa());
//            Negociacao ultimaNegociacao = negociacaoService.buscarUltimaNegociacao(ultimoReajuste);
//
//            BigDecimal economiaTotal = BigDecimal.valueOf(0);
//            if (ultimaNegociacao.getValorFinal() != null) {
//                economiaTotal = BigDecimal.valueOf(ultimaNegociacao.getValorComPrimeiraPorcentagem().doubleValue() - ultimaNegociacao.getValorFinal().doubleValue());
//            }
//
//            System.out.println(ultimaNegociacao.getStatus());
//
//            CardsEmpresaDTO card = CardsEmpresaDTO.builder()
//                .idEmpresa(empresa.getIdEmpresa())
//                .nomeEmpresa(empresa.getNome())
//                .operadora(empresa.getOperadora())
//                .statusNegociacao(ultimaNegociacao.getStatus())
//                .modalidade(empresa.getModalidade())
//                .statusRenovacao(empresa.getStatusRenovacao())
//                .aniversario(empresa.getDtAniversario())
//                .ultimoReajuste(ultimoReajuste.getAnoReferencia())
//                .economiaTotal(economiaTotal)
//                .porcentagemUltimoReajuste(ultimaNegociacao.getPorcentagemFechada())
//                .build();
//
//
//            empresasBuscadas.add(card);
//
//        }
//
//        return empresasBuscadas;
//
//    }

    public EmpresaDTO buscarEmpresa(Integer id) {

        Empresa empresaEncontrada = empresaRepository.findById(id).orElseThrow();

        return EmpresaDTO.builder()
                .nomeEmpresa(empresaEncontrada.getNome())
                .statusRenovacao(empresaEncontrada.getStatusRenovacao())
                .operadora(empresaEncontrada.getOperadora())
                .modalidade(empresaEncontrada.getModalidade())
                .build();

    }
}
