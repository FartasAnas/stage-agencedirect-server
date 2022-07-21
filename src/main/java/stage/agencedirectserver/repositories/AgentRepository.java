package stage.agencedirectserver.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import stage.agencedirectserver.entities.Agent;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    Agent findByUsername(String username);
}
