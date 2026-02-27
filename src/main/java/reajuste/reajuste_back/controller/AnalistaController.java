package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reajuste.reajuste_back.dtos.analista.AnalistaCreateDTO;
import reajuste.reajuste_back.dtos.analista.AnalistasDTO;
import reajuste.reajuste_back.service.AnalistaService;

import java.util.List;

@RestController
@RequestMapping("/analistas")
@RequiredArgsConstructor
public class AnalistaController {

    private final AnalistaService analistaService;

    @GetMapping
    public ResponseEntity<List<AnalistasDTO>> listarAnalistas() {

        List<AnalistasDTO> analistas = analistaService.listarAnalistas();

        return ResponseEntity.ok(analistas);

    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody AnalistaCreateDTO body){

        Boolean usuarioCriado = analistaService.cadastrarAnalista(body);

        if (usuarioCriado) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuário criado com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar o usuário");
        }
    }
}
