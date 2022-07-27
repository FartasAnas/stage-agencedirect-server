package stage.agencedirectserver.utils.affectation;

import lombok.extern.slf4j.Slf4j;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.repositories.AgenceRepository;

@Slf4j
public class ClientToAgenceUtil {
    public static Agence affectAgence(Client client,AgenceRepository agenceRepository) {
        Agence agence=null;
        try {
            if (client.getAgence().getId() != null) {
                agence = agenceRepository.findById( client.getAgence().getId()).orElseThrow();
            } else if (client.getAgence().getNom() != null) {
                agence = agenceRepository.findByNom(client.getAgence().getNom());
            } else {
                throw new Exception("Agence not found");
            }
        }catch (Exception e){
            log.error("Agence is null");
        }
        return agence;
    }
}
