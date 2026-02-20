package reajuste.reajuste_back.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "analista")
@Getter
@NoArgsConstructor
@Setter
public class Analista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_analista")
    private Integer idAnalista;

    private String nome;

}
