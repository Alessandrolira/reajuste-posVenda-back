package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.empresas.*;
import reajuste.reajuste_back.entity.*;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;
import reajuste.reajuste_back.repository.AnalistaRepository;
import reajuste.reajuste_back.repository.EmpresaRepository;
import reajuste.reajuste_back.repository.NegociacaoRepository;
import reajuste.reajuste_back.repository.ReajusteRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

        return BigDecimal.valueOf(soma/contagemElementos);


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

    public List<CardsEmpresaDTO> gerarCardsEmpresas() {

        List<Empresa> empresas = empresaRepository.findAll();
        List<CardsEmpresaDTO> empresasBuscadas = new ArrayList<>();

        for (Empresa empresa : empresas){

            if(reajusteService.buscarUltimoReajuste(empresa) == null){
                CardsEmpresaDTO card = CardsEmpresaDTO.builder()
                        .idEmpresa(empresa.getIdEmpresa())
                        .nomeEmpresa(empresa.getNome())
                        .operadora(empresa.getOperadora())
                        .modalidade(empresa.getModalidade())
                        .statusRenovacao(empresa.getStatusRenovacao())
                        .aniversario(empresa.getDtAniversario())
                        .ultimoReajuste(0)
                        .economiaTotal(BigDecimal.valueOf(0))
                        .porcentagemUltimoReajuste(BigDecimal.valueOf(0))
                        .build();

                empresasBuscadas.add(card);

                continue;
            }

            Reajuste ultimoReajuste = reajusteService.buscarUltimoReajuste(empresa);
            Negociacao ultimaNegociacao = negociacaoService.buscarUltimaNegociacao(ultimoReajuste);

            CardsEmpresaDTO card = CardsEmpresaDTO.builder()
                    .idEmpresa(empresa.getIdEmpresa())
                    .nomeEmpresa(empresa.getNome())
                    .operadora(empresa.getOperadora())
                    .modalidade(empresa.getModalidade())
                    .statusRenovacao(empresa.getStatusRenovacao())
                    .aniversario(empresa.getDtAniversario())
                    .ultimoReajuste(ultimoReajuste.getAnoReferencia())
                    .economiaTotal(BigDecimal.valueOf(ultimaNegociacao.getValorComPrimeiraPorcentagem().doubleValue() - ultimaNegociacao.getValorFinal().doubleValue()))
                    .porcentagemUltimoReajuste(ultimaNegociacao.getPorcentagemFechada())
                    .build();

            empresasBuscadas.add(card);

        }

        return empresasBuscadas;

    }

    public EmpresaDTO buscarEmpresa(Integer id) {

        Empresa empresa = empresaRepository.findById(id).orElseThrow();

        Reajuste ultimoReajuste = null;
        Negociacao ultimaNegociacao = null;
        BigDecimal reajusteReal = BigDecimal.valueOf(0);
        BigDecimal diferencaPercentual = BigDecimal.valueOf(0);
        BigDecimal ultimaNegociacaoPorcentagemOperadora = BigDecimal.valueOf(0);
        BigDecimal ultimaNegociacaoPorcentagemFechada = BigDecimal.valueOf(0);
        BigDecimal valorUltimaFatura = BigDecimal.valueOf(0);
        BigDecimal ultimaNegociacaoValorFechado = BigDecimal.valueOf(0);

        if (reajusteRepository.existsByEmpresa(empresa)){
            ultimoReajuste = reajusteService.buscarUltimoReajuste(empresa);
            if (negociacaoRepository.existsByReajuste(ultimoReajuste)){
                ultimaNegociacao = negociacaoService.buscarUltimaNegociacao(ultimoReajuste);
                reajusteReal = reajusteService.calcularEconomiaReal(ultimaNegociacao, ultimoReajuste);
                diferencaPercentual = reajusteService.calcularDiferencaPercentual(ultimaNegociacao);
                ultimaNegociacaoPorcentagemOperadora = ultimaNegociacao.getPorcentagemPropostaOperadora();
                ultimaNegociacaoPorcentagemFechada = ultimaNegociacao.getPorcentagemFechada();
                valorUltimaFatura = ultimoReajuste.getValorUltimaFatura();
                ultimaNegociacaoValorFechado = ultimaNegociacao.getValorFinal();
            }
        }

        List<Negociacao> negociacoes = new ArrayList<>();;
        if(!negociacaoRepository.findByReajusteEmpresa(empresa).isEmpty()){
            negociacoes = negociacaoRepository.findByReajusteEmpresa(empresa);
        }


        List<PorcentagensFinaisIniciaisDTO> porcentagensFinaisIniciais = new ArrayList<>();
        List<LinhaTempoDTO> linhaTempoCompleta = new ArrayList<>();

        for (Negociacao negociacao : negociacoes){

            PorcentagensFinaisIniciaisDTO porcentagemInicialFinal = PorcentagensFinaisIniciaisDTO.builder()
                    .operadora(negociacao.getPorcentagemPropostaOperadora())
                    .corretora(negociacao.getPorcentagemFechada())
                    .motivoEncerramento(negociacao.getMotivoEncerramento())
                    .build();

            porcentagensFinaisIniciais.add(porcentagemInicialFinal);

            LinhaTempoDTO linhaTempo = LinhaTempoDTO.builder()
                    .ano(negociacao.getReajuste().getAnoReferencia())
                    .economiaPorcentagem(BigDecimal.valueOf(negociacao.getPorcentagemPropostaOperadora().doubleValue() - negociacao.getPorcentagemFechada().doubleValue()))
                    .porcentagemFechada(negociacao.getPorcentagemFechada())
                    .build();

            linhaTempoCompleta.add(linhaTempo);
        }

        List<HistoricoInteracaoDTO> historicoInteracao = interacaoService.buscarInteracoes(empresa);

        return EmpresaDTO.builder()
                .nomeEmpresa(empresa.getNome())
                .statusRenovacao(empresa.getStatusRenovacao())
                .ultimoReajusteOferecido(ultimaNegociacaoPorcentagemOperadora)
                .ultimoReajusteFechado(ultimaNegociacaoPorcentagemFechada)
                .economiaPercentual(diferencaPercentual)
                .economiaReal(reajusteReal)
                .valorUltimaFatura(valorUltimaFatura)
                .valorFechado(ultimaNegociacaoValorFechado)
                .porcentagensFinaisIniciais(porcentagensFinaisIniciais)
                .linhaTempo(linhaTempoCompleta)
                .historicoInteracao(historicoInteracao)
                .build();

    }
}
