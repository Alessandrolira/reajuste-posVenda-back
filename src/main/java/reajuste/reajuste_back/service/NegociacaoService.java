package reajuste.reajuste_back.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.negociacao.NegociacaoResponseDTO;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.entity.Reajuste;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;
import reajuste.reajuste_back.repository.NegociacaoRepository;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class NegociacaoService {

    private final NegociacaoRepository negociacaoRepository;

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
}
