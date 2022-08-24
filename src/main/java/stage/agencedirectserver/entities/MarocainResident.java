package stage.agencedirectserver.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity @Data @DiscriminatorValue("marocain_resident")
@NoArgsConstructor @AllArgsConstructor @Table(name = "marocain_resident")
public class MarocainResident extends Client{
    @Column(nullable = true)
    private String travail;

    private String profession;

    private String type="Particulier résidant au Maroc";
}
