package stage.agencedirectserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.agencedirectserver.entities.Pack;

public interface PackRepository extends JpaRepository<Pack, Long> {
    Pack findByNom(String Nom);
}
