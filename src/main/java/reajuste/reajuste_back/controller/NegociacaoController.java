package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reajuste.reajuste_back.dtos.negociacao.RequestNegociacaoAprovadaDTO;
import reajuste.reajuste_back.dtos.negociacao.ResponseNegociacaoAprovadaDTO;
import reajuste.reajuste_back.service.NegociacaoService;

@RestController
@RequestMapping("/negociacao")
@RequiredArgsConstructor
public class NegociacaoController {

    private final NegociacaoService negociacaoService;

    @PostMapping("/aprovarNegociacao")
    public ResponseEntity<ResponseNegociacaoAprovadaDTO> aprovarNegociacao(@Valid @RequestBody RequestNegociacaoAprovadaDTO body){

        ResponseNegociacaoAprovadaDTO response = negociacaoService.aprovarNegociacao(body);

        return ResponseEntity.ok(response);

    }

}
