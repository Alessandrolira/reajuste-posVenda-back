package reajuste.reajuste_back.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.dtos.analista.AnalistaCreateDTO;
import reajuste.reajuste_back.dtos.analista.AnalistasDTO;
import reajuste.reajuste_back.entity.Analista;
import reajuste.reajuste_back.repository.AnalistaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalistaService {

    private final AnalistaRepository analistaRepository;

    public List<AnalistasDTO> listarAnalistas() {

        List<Analista> analistasEncontrados = analistaRepository.findAll();
        List<AnalistasDTO> analistasTratados = new ArrayList<>();

        analistasEncontrados.forEach(analista -> {
            analistasTratados.add(AnalistasDTO.builder()
                    .nome(analista.getNome())
                    .id(analista.getIdAnalista())
                    .build());
        });

        return analistasTratados;

    }

    public Boolean cadastrarAnalista(AnalistaCreateDTO body) {

        Analista analista = new Analista();

        System.out.println(body.nome());
        analista.setNome(body.nome());

        try {
            analistaRepository.save(analista);
            return true;
        } catch (Exception e){
            System.out.println("Erro ao cadastrar o analista: " + analista.getNome());
            return false;
        }




    }
}
