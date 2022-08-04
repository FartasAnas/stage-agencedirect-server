package stage.agencedirectserver.entities;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stage.agencedirectserver.utils.ToLowerCaseDeserializer;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data @Table(name = "agent")
@NoArgsConstructor @AllArgsConstructor
public class Agent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) @Size(max = 20)
    private String prenom;

    @Column(nullable = false) @Size(max = 20)
    private String nom;

    @Column(nullable = false,unique = true) @Size(max = 10)
    @JsonDeserialize(converter = ToLowerCaseDeserializer.class)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agence_id")
    @JsonIncludeProperties(value = {"id","nom","ville","adresse"})
    private Agence agence;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles=new ArrayList<>();
}
