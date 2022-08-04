package stage.agencedirectserver.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import stage.agencedirectserver.utils.ToLowerCaseDeserializer;

import javax.persistence.*;

@Entity @Data @Table(name = "pack")
@NoArgsConstructor @AllArgsConstructor
public class Pack {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    @JsonDeserialize(converter = ToLowerCaseDeserializer.class)
    private String nom;

    @Column(nullable = false)
    private String tarification;

    @Column(nullable = true)
    private String photoCarte;
}
