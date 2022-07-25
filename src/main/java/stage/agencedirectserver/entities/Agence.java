package stage.agencedirectserver.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data @Table(name = "agence")
@NoArgsConstructor @AllArgsConstructor
public class Agence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true) @Size(max = 20)
    private String nom;

    @Column(nullable = false) @Size(max = 20)
    private String ville;

    @Column(nullable = false) @Size(max = 20)
    private String  adresse;

    @Column(nullable = false)
    private String x;

    @Column(nullable = false)
    private String y;

    @OneToMany(mappedBy = "agence",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JsonIncludeProperties(value = {"id","prenom","nom","username"})
    private Collection<Agent> agents=new ArrayList<>();
}
