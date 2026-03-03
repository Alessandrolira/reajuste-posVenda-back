package reajuste.reajuste_back.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.empresas.CriarEmpresaDTO;
import reajuste.reajuste_back.entity.Analista;
import reajuste.reajuste_back.entity.Empresa;
import reajuste.reajuste_back.enums.empresa.EnumStatusRenovacao;
import reajuste.reajuste_back.repository.AnalistaRepository;
import reajuste.reajuste_back.repository.EmpresaRepository;
import reajuste.reajuste_back.repository.ReajusteRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;
    private final AnalistaRepository analistaRepository;

    public Boolean criarEmpresa(@Valid CriarEmpresaDTO body) throws Exception {


        try {
            Analista analistaEncontrado = analistaRepository.findById(body.idAnalista()).orElseThrow(() -> new Exception("Erro ao encontraro analista"));

            EnumStatusRenovacao statusRenovacao = verificarStatusRenovacao(body.aniversario());


            Empresa empresa = new Empresa();

            empresa.setNome(body.nomeEmpresa());
            empresa.setStatusContrato(body.statusContrato());
            empresa.setOperadora(body.operadora());
            empresa.setDtAniversario(body.aniversario());
            empresa.setPorte(body.porte());
            empresa.setStatusRenovacao(statusRenovacao);
            empresa.setIdAnalista(analistaEncontrado);

            empresaRepository.save(empresa);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar a Empresa " + e);
            return false;
        }


    }

    private EnumStatusRenovacao verificarStatusRenovacao(LocalDate aniversarioOriginal) {

        LocalDate hoje = LocalDate.now();

        // Cria aniversário no ano atual
        LocalDate aniversarioEsteAno = aniversarioOriginal.withYear(hoje.getYear());

        if (aniversarioEsteAno.isBefore(hoje)) {
            aniversarioEsteAno = aniversarioEsteAno.plusYears(1);
        }

        long dias = ChronoUnit.DAYS.between(hoje, aniversarioEsteAno);

        if (dias <= 90) {
            return EnumStatusRenovacao.PENDENTE;
        } else {
            return EnumStatusRenovacao.REAJUSTADO;
        }
    }

    public Long totalEmpresas() {

        return empresaRepository.count();

    }
}
