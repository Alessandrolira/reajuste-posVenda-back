package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reajuste.reajuste_back.dtos.interacao.RequestInteracaoAprovadaDTO;
import reajuste.reajuste_back.dtos.interacao.RequestInteracaoDTO;
import reajuste.reajuste_back.dtos.interacao.ResponseInteracaoDTO;
import reajuste.reajuste_back.service.InteracaoService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/interacao")
public class IntercaoController {

    private final InteracaoService interacaoService;

    @PostMapping
    public ResponseEntity<ResponseInteracaoDTO> create(@Valid @RequestBody RequestInteracaoDTO body) throws Exception {

        ResponseInteracaoDTO response = interacaoService.criarInteracao(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/interacaoAprovada")
    public ResponseEntity<ResponseInteracaoDTO> aprovarInteracao(@Valid @RequestBody RequestInteracaoAprovadaDTO body) throws Exception {

        ResponseInteracaoDTO response = interacaoService.aprovarInteracao(body);

        return ResponseEntity.ok(response);

    }

}
