package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reajuste.reajuste_back.dtos.empresas.DadosGeraisDTO;
import reajuste.reajuste_back.service.EmpresaService;

@RestController
@RequestMapping("/empresas")
@RequiredArgsConstructor
public class EmpresasController {

    private final EmpresaService empresaService;

    @GetMapping("/dadosGerais")
    public ResponseEntity<DadosGeraisDTO> buscarDadosGerais(){

        Long totalEmpresas = empresaService.buscarTotalEmpresas();
        Long economiaTotal = empresaService.valorEconomiaGeral();



    }



}
