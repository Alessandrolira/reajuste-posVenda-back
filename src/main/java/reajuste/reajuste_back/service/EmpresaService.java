package reajuste.reajuste_back.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reajuste.reajuste_back.repository.EmpresaRepository;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

}
