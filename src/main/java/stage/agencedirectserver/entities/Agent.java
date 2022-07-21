package stage.agencedirectserver.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data @Table(name = "agent")
@NoArgsConstructor @AllArgsConstructor
public class Agent {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false) @Size(max = 20)
    private String prenom;

    @Column(nullable = false) @Size(max = 20)
    private String nom;

    @Column(nullable = false,unique = true) @Size(max = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles=new ArrayList<>();
}
