package stage.agencedirectserver.utils.affectation;

import lombok.extern.slf4j.Slf4j;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.repositories.AgenceRepository;
@Slf4j
public class AgentToAgence {
    public static Agence affectAgence(Agent agent, AgenceRepository agenceRepository) {
        Agence agence=null;
        try {
            if (agent.getAgence().getId() != null) {
                agence = agenceRepository.findById( agent.getAgence().getId()).orElseThrow(null);
            } else if (agent.getAgence().getNom() != null) {
                agence = agenceRepository.findByNom(agent.getAgence().getNom());
            } else {
                throw new Exception("Agence not found");
            }
        }catch (Exception e){
            log.error("Agence is null");
        }
        return agence;
    }
}
