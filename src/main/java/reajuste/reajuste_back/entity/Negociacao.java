package reajuste.reajuste_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reajuste.reajuste_back.enums.negociacao.EnumStatusNegociacao;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "negociacao")
@NoArgsConstructor
@Getter
@Setter
public class Negociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_negociacao")
    private Integer idNegociacao;

    @OneToOne
    @JoinColumn(name = "id_reajuste")
    private Reajuste reajuste;

    @Enumerated(EnumType.STRING)
    private EnumStatusNegociacao status;

    @Column(name = "dt_inicio")
    private LocalDate dtInicio;

    @Column(name = "dt_fim")
    private LocalDate dtFim;

    @Column(name = "motivo_encerramento")
    private String motivoEncerramento;

    @OneToMany(
            mappedBy = "negociacao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Interacao> interacoes;


}
