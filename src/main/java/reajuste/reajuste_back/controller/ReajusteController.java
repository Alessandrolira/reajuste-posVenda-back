package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reajuste.reajuste_back.dtos.reajuste.ReajusteCreateDTO;
import reajuste.reajuste_back.dtos.reajuste.ReajusteResponseDTO;
import reajuste.reajuste_back.service.ReajusteService;

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

}
