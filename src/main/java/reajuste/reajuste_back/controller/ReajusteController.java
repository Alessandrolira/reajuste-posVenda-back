package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reajuste.reajuste_back.dtos.empresas.HistoricoInteracaoDTO;
import reajuste.reajuste_back.dtos.reajuste.ReajusteCreateDTO;
import reajuste.reajuste_back.dtos.reajuste.ReajusteResponseDTO;
import reajuste.reajuste_back.dtos.reajuste.UltimoReajusteDTO;
import reajuste.reajuste_back.service.ReajusteService;

import java.util.List;

@RestController
@RequestMapping("/reajuste")
@AllArgsConstructor
public class ReajusteController {

    private final ReajusteService reajusteService;

    @PostMapping
    public ResponseEntity<ReajusteResponseDTO> create(@Valid @RequestBody ReajusteCreateDTO body) throws Exception {

        ReajusteResponseDTO reajusteCriado = reajusteService.criarReajuste(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(reajusteCriado);

    }

    @GetMapping("/ultimoReajuste/{id}")
    public ResponseEntity<UltimoReajusteDTO> buscarUltimoReajuste(@PathVariable("id") Integer id){

        UltimoReajusteDTO ultimoReajuste = reajusteService.buscarUltimoReajusteAprovado(id);

        if (ultimoReajuste == null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.ok(ultimoReajuste);
        }

    }

}
