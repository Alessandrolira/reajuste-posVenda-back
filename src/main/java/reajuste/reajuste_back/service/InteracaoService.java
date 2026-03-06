package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.empresas.HistoricoInteracaoDTO;
import reajuste.reajuste_back.dtos.interacao.RequestInteracaoAprovadaDTO;
import reajuste.reajuste_back.dtos.interacao.RequestInteracaoDTO;
import reajuste.reajuste_back.dtos.interacao.ResponseInteracaoDTO;
import reajuste.reajuste_back.dtos.negociacao.NegociacaoEmAbertoDTO;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Interacao;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;
import reajuste.reajuste_back.repository.InteracaoRepository;
import reajuste.reajuste_back.repository.NegociacaoRepository;
import reajuste.reajuste_back.repository.ReajusteRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InteracaoService {

    private final InteracaoRepository interacaoRepository;
    private final ReajusteRepository reajusteRepository;
    private final NegociacaoRepository negociacaoRepository;
    private final UtilsService utilsService;

    public ResponseInteracaoDTO criarInteracao(@Valid RequestInteracaoDTO body, Integer idReajuste) throws Exception {

        Reajuste reajuste = reajusteRepository.findById(idReajuste).orElseThrow();
        Negociacao negociacao = negociacaoRepository.findAllByReajuste(reajuste);

        Interacao interacao = new Interacao();

        interacao.setNegociacao(negociacao);
        interacao.setTipoInteracao(body.solicitante());
        interacao.setPorcentagemProposta(body.proposta());

        BigDecimal valorResultante = utilsService.calcularReajuste(reajuste.getValorUltimaFatura(), body.proposta());

        interacao.setValorMensalResultante(valorResultante);
        interacao.setObservacao(body.observacoes());
        interacao.setDtInteracao(body.dataProposta());
        interacao.setAceita(false);

        interacaoRepository.save(interacao);

        return ResponseInteracaoDTO.builder()
                .idInteracao(interacao.getIdInteracao())
                .idNegociacao(interacao.getNegociacao().getIdNegociacao())
                .tipo(interacao.getTipoInteracao())
                .valorMensalResultante(interacao.getValorMensalResultante())
                .observacao(interacao.getObservacao())
                .dtInteracao(interacao.getDtInteracao())
                .isAceita(interacao.getAceita())
                .build();

    }

    public ResponseInteracaoDTO aprovarInteracao(@Valid RequestInteracaoAprovadaDTO body) throws Exception {

        Interacao interacao = interacaoRepository.findById(body.idInteracao()).orElseThrow();
        Negociacao negociacao = negociacaoRepository.findById(interacao.getNegociacao().getIdNegociacao()).orElseThrow();

        if(interacaoRepository.existsByNegociacao_IdNegociacaoAndAceitaTrue(negociacao.getIdNegociacao())){
            throw new Exception("Já existe uma negociação aceita para essa proposta");
        } else {
            interacao.setAceita(true);
            interacaoRepository.save(interacao);
            negociacao.setStatus(EnumStatusNegociacao.INTERACAO_ACEITA);
            negociacao.setDtFim(body.dataAceite());
            negociacao.setMotivoEncerramento(body.motivoEncerramento());

            BigDecimal ultimoValorAceitado = utilsService.calcularReajuste(negociacao.getReajuste().getValorUltimaFatura() ,interacao.getPorcentagemProposta());
            negociacao.setPorcentagemFechada(interacao.getPorcentagemProposta());
            negociacao.setValorFinal(ultimoValorAceitado);
            negociacao.setValorComPrimeiraPorcentagem(negociacao.getReajuste().getVlComPrimeiraPorcentagem());

            negociacaoRepository.save(negociacao);
        }

        return ResponseInteracaoDTO.builder()
                .idInteracao(interacao.getIdInteracao())
                .isAceita(interacao.getAceita())
                .build();


    }

    public List<HistoricoInteracaoDTO> buscarInteracao(NegociacaoEmAbertoDTO negociacaoEnviada) {

        Reajuste reajuste = reajusteRepository.findById(negociacaoEnviada.idReajuse()).orElseThrow();
        Negociacao negociacao = negociacaoRepository.findAllByReajuste(reajuste);

        List<Interacao> interacoesEncontradas = interacaoRepository.findAllByNegociacao(negociacao);
        List<HistoricoInteracaoDTO> interacoes = new ArrayList<>();

        for (Interacao interacao : interacoesEncontradas){

            HistoricoInteracaoDTO historicoInteracao = HistoricoInteracaoDTO.builder()
                    .id(interacao.getIdInteracao())
                    .ano(negociacao.getReajuste().getAnoReferencia())
                    .tipo(interacao.getTipoInteracao())
                    .porcentagemProposta(interacao.getPorcentagemProposta())
                    .valorAtual(negociacao.getValorInicial())
                    .vlMensalResultante(utilsService.calcularReajuste(negociacao.getValorInicial(), interacao.getPorcentagemProposta()))
                    .dtInteracao(interacao.getDtInteracao())
                    .observacao(interacao.getObservacao())
                    .isAceita(interacao.getAceita())
                    .build();

            interacoes.add(historicoInteracao);

        }

        return interacoes;

    }
}
