package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.MarocainResident;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.MarocainResidentRepository;
import stage.agencedirectserver.repositories.PackRepository;
import stage.agencedirectserver.utils.CodeAccessGenerator;
import stage.agencedirectserver.utils.ExpireDateUtil;
import stage.agencedirectserver.utils.UpdateClientUtils;
import stage.agencedirectserver.utils.affectation.ClientToAgenceUtil;
import stage.agencedirectserver.utils.affectation.ClientToPackUtil;
import stage.agencedirectserver.utils.sendmail.EmailService;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class MarocainResidentService {
    private final MarocainResidentRepository marocainResidentRepository;
    private final AgenceRepository agenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PackRepository packRepository;

    // get Methods
    public List<MarocainResident> getAllMarocainResidents() { return marocainResidentRepository.findAll(); }
    public MarocainResident getMarocainResidentByEmail(String email) { return marocainResidentRepository.findByEmail(email); }
    public MarocainResident getMarocainResidentByCIN(String cin) { return marocainResidentRepository.findByCIN(cin); }

    // add Methods
    public MarocainResident addMarocainResident(MarocainResident marocainResident) throws MessagingException, NotFoundException, NullAttributeException {
        //check if travail is null
        if (marocainResident.getTravail() == null) {
            throw new NullAttributeException("travail is null");
        }
        //Generate a Random 8 character String
        marocainResident.setCodeAccess(CodeAccessGenerator.generate());

        //Generate the Expiration Date 4 years from current date
        marocainResident.setDateExpiration(ExpireDateUtil.getExpireDate(4));

        //Affect agence if not null
        if(marocainResident.getAgence() != null)
            marocainResident.setAgence(ClientToAgenceUtil.affectAgence(marocainResident, agenceRepository));

        //Affect pack if not null
        if(marocainResident.getPack() != null)
            marocainResident.setPack(ClientToPackUtil.affectPack(marocainResident,packRepository));

        //Send Mail to Client  off for the moment
        //String Status=emailService.sendSimpleMail(new EmailDetails(etudiant.getEmail(),etudiant.getCodeAccess()));

        //crypt the password
        marocainResident.setCodeAccess(passwordEncoder.encode(marocainResident.getCodeAccess()));

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


