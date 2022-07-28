package stage.agencedirectserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.agencedirectserver.entities.Client;

public interface ClientBaseRepository <T extends Client> extends JpaRepository <T,Long> {
    T findByEmail(String email);
    T findByCIN(String cin);
}
