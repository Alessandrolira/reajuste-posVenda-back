package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.negociacao.NegociacaoResponseDTO;
import reajuste.reajuste_back.dtos.negociacao.RequestNegociacaoAprovadaDTO;
import reajuste.reajuste_back.dtos.negociacao.ResponseNegociacaoAprovadaDTO;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;
import reajuste.reajuste_back.repository.EmpresaRepository;
import reajuste.reajuste_back.repository.NegociacaoRepository;
import reajuste.reajuste_back.repository.ReajusteRepository;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class NegociacaoService {

    private final NegociacaoRepository negociacaoRepository;
    private final EmpresaRepository empresaRepository;

    public Negociacao criarNegociacao(Reajuste reajuste) {

        try {
            Negociacao negociacao = new Negociacao();

            negociacao.setReajuste(reajuste);
            negociacao.setStatus(EnumStatusNegociacao.EM_ANDAMENTO);
            negociacao.setDtInicio(LocalDate.now());
            negociacao.setValorInicial(reajuste.getValorUltimaFatura());
            negociacao.setPorcentagemPropostaOperadora(reajuste.getPorcentagemOperadora());

            return negociacaoRepository.save(negociacao);

        } catch (Exception e){
            throw new RuntimeException("Erro ao criar a negociacao");
        }

    }

    public ResponseNegociacaoAprovadaDTO aprovarNegociacao(@Valid RequestNegociacaoAprovadaDTO body) {

        Negociacao negociacao = negociacaoRepository.findById(body.idNegociacao()).orElseThrow(() -> new RuntimeException("Erro ao encontrar a negociacao"));
        Empresa empresa = empresaRepository.findById(negociacao.getReajuste().getEmpresa().getIdEmpresa()).orElseThrow(() ->  new RuntimeException("Erro ao encontrar a empresa"));

        negociacao.setStatus(EnumStatusNegociacao.FINALIZADA);
        negociacaoRepository.save(negociacao);

        empresa.setStatusRenovacao(EnumStatusRenovacao.REAJUSTADO);
        empresaRepository.save(empresa);

        return ResponseNegociacaoAprovadaDTO.builder()
                .idNegociacao(negociacao.getIdNegociacao())
                .build();


    }
}
