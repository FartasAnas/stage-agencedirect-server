package stage.agencedirectserver.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data @Table(name = "etudiant")
@NoArgsConstructor @AllArgsConstructor
@DiscriminatorValue("Etudiant")
public class Etudiant extends Client {
    @Column(nullable = false)
    private String ecole;
}
