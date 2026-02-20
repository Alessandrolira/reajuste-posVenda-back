package reajuste.reajuste_back.entity;

import jakarta.persistence.*;
import lombok.*;
import reajuste.reajuste_back.enums.reajuste.EnumStatusReajuste;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "reajuste")
@NoArgsConstructor
@Getter
@Setter
public class Reajuste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reajuste")
    private Integer idReajuste;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @Column(name = "ano_referencia")
    private LocalDate anoReferencia;

    @Enumerated(EnumType.STRING)
    private EnumStatusReajuste status;

    @Column(name = "vl_ultima_fatura")
    private BigDecimal valorUltimaFatura;

    @Column(name = "porcentagem_oferecida_operadora")
    private BigDecimal porcentagemOperadora;

    @Column(name = "vl_com_primeira_porcentagem")
    private BigDecimal vlComPrimeiraPorcentagem;

    @Column(name = "dt_envio")
    private LocalDate dtEnvio;

    @OneToMany(mappedBy = "reajuste", cascade = CascadeType.ALL)
    private List<Documentos> documentos;


}
