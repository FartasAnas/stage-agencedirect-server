package stage.agencedirectserver.utils.affectation;

import lombok.extern.slf4j.Slf4j;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.PackRepository;

@Slf4j
public class ClientToPackUtil {
    public static Pack affectPack(Client client, PackRepository packRepository) throws NotFoundException {
        Pack pack=null;
        try {
            if (client.getPack().getId() != null) {
                log.info("pack id :{}",client.getPack().getId());
                pack = packRepository.findById( client.getPack().getId()).orElseThrow(null);
            } else if (client.getPack().getNom() != null) {
                pack = packRepository.findByNom(client.getPack().getNom());
            } else {
                throw new NotFoundException("Pack was not found");
            }
        }catch (Exception e){
            throw new NotFoundException("Pack was not found");
        }
        return pack;
    }
}
