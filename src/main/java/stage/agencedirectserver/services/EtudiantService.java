package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Etudiant;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.EtudiantRepository;
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
public class EtudiantService {
    private final EtudiantRepository etudiantRepository;
    private final AgenceRepository agenceRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PackRepository packRepository;

    // get Methods
    public List<Etudiant> getAllEtudiants() { return etudiantRepository.findAll(); }
    public Etudiant getEtudiantByEmail(String email) { return etudiantRepository.findByEmail(email); }
    public Etudiant getEtudiantByCIN(String cin) { return etudiantRepository.findByCIN(cin); }

    // add Methods
    public Etudiant addEtudiant(Etudiant etudiant) throws MessagingException, NotFoundException, NullAttributeException {
        //check if Ecole is null
        if (etudiant.getEcole() == null) {
            throw new NullAttributeException("Ecole is null");
        }

        //Generate a Random 8 character String
        etudiant.setCodeAccess(CodeAccessGenerator.generate());

        //Generate the Expiration Date 4 years from current date
        etudiant.setDateExpiration(ExpireDateUtil.getExpireDate(4));

        //Affect agence if not null
        if(etudiant.getAgence() != null)
            etudiant.setAgence(ClientToAgenceUtil.affectAgence(etudiant, agenceRepository));

        //Affect pack if not null
        if(etudiant.getPack() != null)
            etudiant.setPack(ClientToPackUtil.affectPack(etudiant,packRepository));

        //Send Mail to Client  off for the moment
        //String Status=emailService.sendSimpleMail(new EmailDetails(etudiant.getEmail(),etudiant.getCodeAccess()));

        //crypt the password
        etudiant.setCodeAccess(passwordEncoder.encode(etudiant.getCodeAccess()));

        return etudiantRepository.save(etudiant);
    }

    // update Methods
    public Etudiant updateEtudiant(Long id, Etudiant etudiant) throws NotFoundException {
        Etudiant etudiantToUpdate = etudiantRepository.findById(id).orElseThrow(() -> new NotFoundException("Etudiant was not found"));
        UpdateClientUtils.update(etudiantToUpdate,etudiant);
        etudiantToUpdate.setEcole(etudiant.getEcole()!=null ? etudiant.getEcole() : etudiantToUpdate.getEcole());
        return etudiantToUpdate;
    }

    // delete Methods
    public void deleteEtudiant(Long id) {
        etudiantRepository.deleteById(id);
    }


}
