package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import reajuste.reajuste_back.dtos.interacao.RequestInteracaoAprovadaDTO;
import reajuste.reajuste_back.dtos.interacao.RequestInteracaoDTO;
import reajuste.reajuste_back.dtos.interacao.ResponseInteracaoDTO;
import reajuste.reajuste_back.dtos.negociacao.NegociacaoResponseDTO;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class InteracaoService {

    private final InteracaoRepository interacaoRepository;
    private final NegociacaoService negociacaoService;
    private final ReajusteRepository reajusteRepository;
    private final ReajusteService reajusteService;
    private final NegociacaoRepository negociacaoRepository;

    public ResponseInteracaoDTO criarInteracao(@Valid RequestInteracaoDTO body) throws Exception {

        Negociacao negociacaoCriada = null;
        try {
            Reajuste reajusteEncontrado = reajusteRepository.findById(body.idReajuste()).orElseThrow();
            Empresa empresa = reajusteEncontrado.getEmpresa();
            Boolean existeNegociacao = negociacaoRepository.existsByReajuste_IdReajuste(reajusteEncontrado.getIdReajuste());

            if (empresa.getStatusRenovacao() == EnumStatusRenovacao.EM_NEGOCIACAO && existeNegociacao) {
                negociacaoCriada = negociacaoRepository.findByReajuste_IdReajuste(reajusteEncontrado.getIdReajuste());
            } else {
                negociacaoCriada = negociacaoService.criarNegociacao(reajusteEncontrado);
                empresa.setStatusRenovacao(EnumStatusRenovacao.EM_NEGOCIACAO);
            }

        } catch (Exception e){
            throw new Exception("Erro ao criar a negociacao " + e );
        }

        Interacao interacao = new Interacao();

        interacao.setNegociacao(negociacaoCriada);
        interacao.setTipoInteracao(body.solicitante());
        interacao.setPorcentagemProposta(body.proposta());

        Reajuste reajuste = reajusteRepository.findById(negociacaoCriada.getReajuste().getIdReajuste()).orElseThrow();
        BigDecimal valorResultante = reajusteService.calcularReajuste(reajuste.getValorUltimaFatura(), body.proposta());

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

            BigDecimal ultimoValorAceitado = reajusteService.calcularReajuste(negociacao.getReajuste().getValorUltimaFatura() ,interacao.getPorcentagemProposta());
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
}
