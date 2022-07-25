package stage.agencedirectserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.agencedirectserver.entities.Agence;

import java.util.List;

public interface AgenceRepository extends JpaRepository<Agence, Long> {
    List<Agence> findAllByVille(String ville);
    Agence findByNom(String nom);
}
