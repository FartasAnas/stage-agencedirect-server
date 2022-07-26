package stage.agencedirectserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.agencedirectserver.entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);
    Client findByCIN(String cin);

}
