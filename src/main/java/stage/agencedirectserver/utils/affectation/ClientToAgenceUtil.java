package stage.agencedirectserver.utils.affectation;

import lombok.extern.slf4j.Slf4j;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.exceptions.notfound.AgenceNotFoundException;
import stage.agencedirectserver.repositories.AgenceRepository;

@Slf4j
public class ClientToAgenceUtil {
    public static Agence affectAgence(Client client,AgenceRepository agenceRepository) throws AgenceNotFoundException {
        Agence agence=null;
        try {
            if (client.getAgence().getId() != null) {
                agence = agenceRepository.findById( client.getAgence().getId()).orElseThrow(AgenceNotFoundException::new);
            } else if (client.getAgence().getNom() != null) {
                agence = agenceRepository.findByNom(client.getAgence().getNom());
            } else {
                throw new AgenceNotFoundException();
            }
        }catch (Exception e){
            throw new AgenceNotFoundException();
        }
        return agence;
    }
}
