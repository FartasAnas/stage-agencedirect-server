package stage.agencedirectserver.utils.affectation;

import lombok.extern.slf4j.Slf4j;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.repositories.PackRepository;

@Slf4j
public class ClientToPackUtil {
    public static Pack affectPack(Client client, PackRepository packRepository) {
        Pack pack=null;
        try {
            if (client.getAgence().getId() != null) {
                pack = packRepository.findById( client.getAgence().getId()).orElseThrow(null);
            } else if (client.getAgence().getNom() != null) {
                pack = packRepository.findByNom(client.getAgence().getNom());
            } else {
                throw new Exception("pack not found");
            }
        }catch (Exception e){
            log.error("pack is null");
        }
        return pack;
    }
}
