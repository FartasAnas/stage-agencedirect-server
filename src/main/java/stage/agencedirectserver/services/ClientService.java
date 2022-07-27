package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.ClientRepository;
import stage.agencedirectserver.repositories.PackRepository;
import stage.agencedirectserver.utils.CodeAccessGenerator;
import stage.agencedirectserver.utils.ExpireDateUtil;
import stage.agencedirectserver.utils.affectation.ClientToAgenceUtil;
import stage.agencedirectserver.utils.affectation.ClientToPackUtil;
import stage.agencedirectserver.utils.sendmail.EmailService;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final AgenceRepository agenceRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final PackRepository packRepository;

    // get Methods
    public List<Client> getAllClients() {
        log.info("Fetching all clients");
        return clientRepository.findAll();
    }
    public Client getClientByEmail(String email) {
        log.info("Fetching client with email {}", email);
        return clientRepository.findByEmail(email);
    }
    public Client getClientByCIN(String cin) {
        log.info("Fetching client with cin {}", cin);
        return clientRepository.findByCIN(cin);
    }

    // add Methods
    public Client addClient(Client client) throws MessagingException {
        //Generate a Random 8 character String
        client.setCodeAccess(CodeAccessGenerator.generate());

        //Generate the Expiration Date 4 years from current date
        client.setDateExpiration(ExpireDateUtil.getExpireDate(4));

        //Affect agence if not null
        client.setAgence(ClientToAgenceUtil.affectAgence(client,agenceRepository));

        //Affect pack if not null
        client.setPack(ClientToPackUtil.affectPack(client,packRepository));

        //Send Mail to Client  off for the moment
        //String Status=emailService.sendSimpleMail(new EmailDetails(client.getEmail(),client.getCodeAccess()));

        //crypt the password
        client.setCodeAccess(passwordEncoder.encode(client.getCodeAccess()));

        log.info("Adding client {}", client);
        return clientRepository.save(client);
    }

    // update Methods
    public Client updateClient(Long id, Client client) {
        log.info("Updating client {}", client);
        Client clientToUpdate = clientRepository.findById(client.getId()).orElseThrow(null);
        clientToUpdate.setEmail(client.getEmail()!=null ? client.getEmail() : clientToUpdate.getEmail());
        clientToUpdate.setCodeAccess(client.getCodeAccess()!=null ? client.getCodeAccess() : clientToUpdate.getCodeAccess());
        clientToUpdate.setNom(client.getNom()!=null ? client.getNom() : clientToUpdate.getNom());
        clientToUpdate.setPrenom(client.getPrenom()!=null ? client.getPrenom() : clientToUpdate.getPrenom());
        clientToUpdate.setAdresse(client.getAdresse()!=null ? client.getAdresse() : clientToUpdate.getAdresse());
        clientToUpdate.setTelephone(client.getTelephone()!=null ? client.getTelephone() : clientToUpdate.getTelephone());
        clientToUpdate.setDateNaissance(client.getDateNaissance()!=null ? client.getDateNaissance() : clientToUpdate.getDateNaissance());
        clientToUpdate.setDateInscription(client.getDateInscription()!=null ? client.getDateInscription() : clientToUpdate.getDateInscription());
        clientToUpdate.setDateExpiration(client.getDateExpiration()!=null ? client.getDateExpiration() : clientToUpdate.getDateExpiration());
        clientToUpdate.setCinRectoURL(client.getCinRectoURL()!=null ? client.getCinRectoURL() : clientToUpdate.getCinRectoURL());
        clientToUpdate.setCinVersoURL(client.getCinVersoURL()!=null ? client.getCinVersoURL() : clientToUpdate.getCinVersoURL());
        clientToUpdate.setSelfieURL(client.getSelfieURL()!=null ? client.getSelfieURL() : clientToUpdate.getSelfieURL());
        clientToUpdate.setAdresse(client.getAdresse()!=null ? client.getAdresse() : clientToUpdate.getAdresse());
        clientToUpdate.setProfession(client.getProfession()!=null ? client.getProfession() : clientToUpdate.getProfession());
        return clientToUpdate;
    }

    // delete Methods
    public void deleteClient(Long id) {
        log.info("Deleting client with id {}", id);
        clientRepository.deleteById(id);
    }

    // other Methods
    public void addClientToAgence(String email,String agenceName){
        Client client = clientRepository.findByEmail(email);
        Agence agence = agenceRepository.findByNom(agenceName);
        if(client == null || agence == null) {
            log.info("Client or Agence not found");
            throw new RuntimeException("Client or Agence not found");
        }

        log.info("adding Client {}", client.getEmail() ," to Agence {}",agence.getNom());
        client.setAgence(agence);
    }

    public void addClientToPack(String email,String packName){
        Client client = clientRepository.findByEmail(email);
        Pack pack = packRepository.findByNom(packName);
        if(client == null || packName == null) {
            log.info("Client or Pack not found");
            throw new RuntimeException("Client or Pack not found");
        }
        log.info("adding Client {}", client.getEmail() ," to Pack {}",pack.getNom());
        client.setPack(pack);
    }

}
