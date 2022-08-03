package stage.agencedirectserver.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Data @DiscriminatorValue("etudiant")
@NoArgsConstructor @AllArgsConstructor @Table(name = "etudiant")
public class Etudiant extends Client {
    @Column(nullable = true)
    private String ecole;

}
