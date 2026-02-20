package reajuste.reajuste_back.entity;

import jakarta.persistence.*;
import lombok.*;
import reajuste.reajuste_back.enums.empresa.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Table(name="empresa")
@NoArgsConstructor
@Getter
public class Empresa {

    @Id
    @Column(name = "id_empresa")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmpresa;

    private String nome;

    @Enumerated(EnumType.STRING)
    private EnumStatusContrato statusContrato;

    @Enumerated(EnumType.STRING)
    private EnumOperadora operadora;

    @Column(name = "dt_aniversario")
    private LocalDate dtAniversario;

    private EnumPorte porte;

    @Column(name = "status_renovacao")
    @Enumerated(EnumType.STRING)
    private EnumStatusRenovacao statusRenovacao;

    @OneToMany(mappedBy = "empresa")
    private List<Reajuste> reajustes;

}
