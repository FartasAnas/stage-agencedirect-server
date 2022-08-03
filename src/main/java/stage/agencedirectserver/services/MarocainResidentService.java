package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.MarocainResident;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.repositories.MarocainResidentRepository;
import stage.agencedirectserver.utils.UpdateClientUtils;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class MarocainResidentService {
    private final MarocainResidentRepository marocainResidentRepository;
    private final ClientService clientService;

    // get Methods
    public List<MarocainResident> getAllMarocainResidents() { return marocainResidentRepository.findAll(); }
    public MarocainResident getMarocainResidentByEmail(String email) { return marocainResidentRepository.findByEmail(email); }
    public MarocainResident getMarocainResidentByCIN(String cin) { return marocainResidentRepository.findByCIN(cin); }

    // add Methods
    public MarocainResident addMarocainResident(MarocainResident marocainResident) throws MessagingException, NotFoundException, NullAttributeException, EmailNotValidException {
        //check if travail is null
        if (marocainResident.getTravail() == null) {
            throw new NullAttributeException("travail is null");
        }
        marocainResident=(MarocainResident) clientService.addClient(marocainResident);
        return marocainResidentRepository.save(marocainResident);
    }

    // update Methods
    public MarocainResident updateMarocainResident(Long id,MarocainResident marocainResident) throws NotFoundException {
        MarocainResident marocainResidentToUpdate = marocainResidentRepository.findById(id).orElseThrow((() -> new NotFoundException("Marocain Resident was not found")));
        UpdateClientUtils.update(marocainResidentToUpdate,marocainResident);
        marocainResidentToUpdate.setTravail(marocainResident.getTravail()!=null ? marocainResident.getTravail() : marocainResidentToUpdate.getTravail());
        return marocainResidentRepository.save(marocainResidentToUpdate);
    }

    // delete Methods
    public void deleteMarocainResident(Long id) { marocainResidentRepository.deleteById(id); }



}


