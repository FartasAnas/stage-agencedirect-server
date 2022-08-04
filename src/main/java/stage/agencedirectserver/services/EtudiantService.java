package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Etudiant;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.repositories.EtudiantRepository;
import stage.agencedirectserver.utils.UpdateClientUtils;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class EtudiantService {
    private final EtudiantRepository etudiantRepository;
    private final ClientService clientService;

    // get Methods
    public List<Etudiant> getAllEtudiants() { return etudiantRepository.findAll(); }
    public Etudiant getEtudiantByEmail(String email) { return etudiantRepository.findByEmail(email.toLowerCase()); }
    public Etudiant getEtudiantByCIN(String cin) { return etudiantRepository.findByCIN(cin.toLowerCase()); }

    // add Methods
    public Etudiant addEtudiant(Etudiant etudiant) throws MessagingException, NotFoundException, NullAttributeException, EmailNotValidException {
        //check if Ecole is null
        if (etudiant.getEcole() == null) {
            throw new NullAttributeException("Ecole is null");
        }
        return etudiantRepository.save((Etudiant) clientService.addClient(etudiant));
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
