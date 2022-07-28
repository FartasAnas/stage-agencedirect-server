package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.PackRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor
public class PackService {
    private final PackRepository packRepository;

    // get Methods
    public List<Pack> getAllPacks(){
        return packRepository.findAll();
    }
    public Pack getPackByTitre(String titre){
        return packRepository.findByNom(titre);
    }

    // post Methods
    public Pack addPack(Pack pack){
        return packRepository.save(pack);
    }

    // update Methods
    public Pack updatePack(Long id , Pack pack) throws NotFoundException {
        Pack packToUpdate=packRepository.findById(id).orElseThrow((() -> new NotFoundException("Pack was not found")));
        packToUpdate.setNom(pack.getNom()!=null ? pack.getNom() : packToUpdate.getNom());
        packToUpdate.setTarification(pack.getTarification()!=null ? pack.getTarification() : packToUpdate.getTarification());
        packToUpdate.setPhotoCarte(pack.getPhotoCarte()!=null ? pack.getPhotoCarte() : packToUpdate.getPhotoCarte());
        return packToUpdate;
    }

    // delete Methods
    public void deletePack(Long id){
        packRepository.deleteById(id);
    }
}
