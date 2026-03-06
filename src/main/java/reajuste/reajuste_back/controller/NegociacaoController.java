package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reajuste.reajuste_back.dtos.empresas.HistoricoInteracaoDTO;
import reajuste.reajuste_back.dtos.negociacao.NegociacaoEmAbertoDTO;
import reajuste.reajuste_back.dtos.negociacao.RequestNegociacaoAprovadaDTO;
import reajuste.reajuste_back.dtos.negociacao.ResponseNegociacaoAprovadaDTO;
import reajuste.reajuste_back.entity.Negociacao;
import reajuste.reajuste_back.service.InteracaoService;
import reajuste.reajuste_back.service.NegociacaoService;

import java.util.List;

@RestController
@RequestMapping("/negociacao")
@RequiredArgsConstructor
public class NegociacaoController {

    private final NegociacaoService negociacaoService;
    private final InteracaoService interacaoService;

    @PostMapping("/aprovarNegociacao")
    public ResponseEntity<ResponseNegociacaoAprovadaDTO> aprovarNegociacao(@Valid @RequestBody RequestNegociacaoAprovadaDTO body){

        ResponseNegociacaoAprovadaDTO response = negociacaoService.aprovarNegociacao(body);

        return ResponseEntity.ok(response);

    }

    @GetMapping("/emAberto/{idEmpresa}")
    public ResponseEntity<NegociacaoEmAbertoDTO> negociacaoEmAberto(@PathVariable("idEmpresa") Integer id){

        NegociacaoEmAbertoDTO encontrarNegocicaoEmAberto = negociacaoService.buscarNegociacaoEmAberto(id);

        if (encontrarNegocicaoEmAberto == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        List<HistoricoInteracaoDTO> interacoes = interacaoService.buscarInteracao(encontrarNegocicaoEmAberto);
        NegociacaoEmAbertoDTO response = negociacaoService.adicionarInteracoes(encontrarNegocicaoEmAberto, interacoes);

        if (response == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
           return ResponseEntity.ok(response);
        }

    }

}
