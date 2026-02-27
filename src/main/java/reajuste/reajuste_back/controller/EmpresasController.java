package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reajuste.reajuste_back.dtos.empresas.CriarEmpresaDTO;
import reajuste.reajuste_back.service.EmpresaService;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresasController {

    private final EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CriarEmpresaDTO body) throws Exception {

        Boolean empresaCriada = empresaService.criarEmpresa(body);

        if (empresaCriada) {
            return ResponseEntity.status(HttpStatus.CREATED).body("Empresa criada com sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao cadastrar a empresa");
        }


    }



}
