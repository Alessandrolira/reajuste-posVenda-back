package reajuste.reajuste_back.controller;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reajuste.reajuste_back.dtos.empresas.*;
import reajuste.reajuste_back.service.EmpresaService;

import java.math.BigDecimal;
import java.util.List;

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

    @GetMapping("/totalEmpresas")
    public ResponseEntity<Long> totalEmpresas() {

        Long totalEmpresas = empresaService.totalEmpresas();

        return ResponseEntity.ok(totalEmpresas);

    }

    @GetMapping("/economiaTotal")
    public ResponseEntity<BigDecimal> economiaTotal() {

        BigDecimal economiaTotal = empresaService.economiaTotal();

        return ResponseEntity.ok(economiaTotal);

    }

    @GetMapping("/mediaReducao")
    public ResponseEntity<BigDecimal> mediaReducao() {

        BigDecimal mediaReducao = empresaService.mediaReducao();

        return ResponseEntity.ok(mediaReducao);

    }

    @GetMapping("/melhorNegociacao")
    public ResponseEntity<EmpresaMelhorNegociacaoDTO> melhorNegociacao() {

        EmpresaMelhorNegociacaoDTO empresaMelhorNegociacao = empresaService.encontrarMelhorNegociacao();

        if(empresaMelhorNegociacao.valorEconomizado() < 0){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.ok(empresaMelhorNegociacao);
        }
    }

//    @GetMapping("/buscarCardsEmpresa")
//    public ResponseEntity<List<CardsEmpresaDTO>> buscarCardsEmpresa(){
//
//        List<CardsEmpresaDTO> cardsEmpresas = empresaService.gerarCardsEmpresas();
//
//        return ResponseEntity.ok(cardsEmpresas);
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> buscarEmpresa(@PathVariable Integer id){

        EmpresaDTO response = empresaService.buscarEmpresa(id);

        return ResponseEntity.ok(response);
    }



}
