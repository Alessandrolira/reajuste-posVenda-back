package reajuste.reajuste_back.entity;

import io.swagger.v3.oas.models.links.Link;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "documentos")
@Getter
@NoArgsConstructor
@Setter
public class Documentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(name = "nome_documento")
    private String nomeDocumento;

    @Column(name = "link_documento")
    private String linkDocumento;

    @ManyToOne
    @JoinColumn(name = "id_reajuste")
    private Reajuste reajuste;

}
