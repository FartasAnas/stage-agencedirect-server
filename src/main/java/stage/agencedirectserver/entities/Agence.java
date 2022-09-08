package stage.agencedirectserver.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import stage.agencedirectserver.utils.ToLowerCaseDeserializer;

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
    @JsonDeserialize(converter = ToLowerCaseDeserializer.class)
    private String nom;

    @Column(nullable = false,unique = true) @Size(max = 20)
    private String codeAgence= RandomStringUtils.random(3, "0123456789");

    @Column(nullable = false) @Size(max = 20)
    private String ville;

    @Column(nullable = false) @Size(max = 50)
    private String  adresse;

    @Column(nullable = false)
    private String x="0"    ;

    @Column(nullable = false)
    private String y="0";

    @OneToMany(mappedBy = "agence",fetch = FetchType.LAZY,cascade=CascadeType.ALL)
    @JsonIncludeProperties(value = {"id","prenom","nom","username"})
    private Collection<Agent> agents=new ArrayList<>();

}
