package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.ClientRepository;
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
public class ClientService {
    private final ClientRepository clientRepository;
    private final AgenceRepository agenceRepository;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final PackRepository packRepository;

    // get Methods
    public List<Client> getAllClients() { return clientRepository.findAll();  }
    public Client getClientByEmail(String email) { return clientRepository.findByEmail(email); }
    public Client getClientByCIN(String cin) { return clientRepository.findByCIN(cin); }

    // add Methods
    public Client addClient(Client client) throws MessagingException, NotFoundException {
        //Generate a Random 8 character String
        client.setCodeAccess(CodeAccessGenerator.generate());

        //Generate the Expiration Date 4 years from current date
        client.setDateExpiration(ExpireDateUtil.getExpireDate(4));

        //Affect agence if not null
        if(client.getAgence() != null)
            client.setAgence(ClientToAgenceUtil.affectAgence(client,agenceRepository));

        //Affect pack if not null
        if(client.getPack() != null)
            client.setPack(ClientToPackUtil.affectPack(client,packRepository));

        //Send Mail to Client  off for the moment
        //String Status=emailService.sendSimpleMail(new EmailDetails(client.getEmail(),client.getCodeAccess()));

        //crypt the password
        client.setCodeAccess(passwordEncoder.encode(client.getCodeAccess()));

        return clientRepository.save(client);
    }

    // update Methods
    public Client updateClient(Long id, Client client) throws NotFoundException {
        Client clientToUpdate = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client was not found"));
        UpdateClientUtils.update(clientToUpdate,client);
        return clientToUpdate;
    }

    // delete Methods
    public void deleteClient(Long id) { clientRepository.deleteById(id); }

    // other Methods
    public void addClientToAgence(String email,String agenceName) throws NotFoundException {
        Client client = clientRepository.findByEmail(email);
        Agence agence = agenceRepository.findByNom(agenceName);
        if(client == null || agence == null) {
            throw new NotFoundException("Client was not found");
        }
        client.setAgence(agence);
    }

    public void addClientToPack(String email,String packName) throws NotFoundException {
        Client client = clientRepository.findByEmail(email);
        Pack pack = packRepository.findByNom(packName);
        if(client == null || packName == null) {
            throw new NotFoundException("Client was not found");
        }
        client.setPack(pack);
    }

}
