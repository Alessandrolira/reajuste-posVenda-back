package reajuste.reajuste_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import reajuste.reajuste_back.enums.interacao.EnumTipoInteracao;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "interacao")
@NoArgsConstructor
@Getter
@Setter
public class Interacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_interacao")
    private Integer idInteracao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_negociacao", nullable = false)
    private Negociacao negociacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private EnumTipoInteracao tipoInteracao;

    @Column(name = "porcentagem_proposta")
    private BigDecimal porcentagemProposta;

    @Column(name = "vl_mensal_resultante")
    private BigDecimal valorMensalResultante;

    @Lob
    private String observacao;

    @Column(name = "dt_interacao")
    private LocalDate dtInteracao;

    @Column(name = "is_aceita")
    private Boolean aceita;

}
