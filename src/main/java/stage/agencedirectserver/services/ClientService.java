package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.*;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.ClientRepository;
import stage.agencedirectserver.repositories.PackRepository;
import stage.agencedirectserver.repositories.RoleRepository;
import stage.agencedirectserver.utils.CodeAccessGenerator;
import stage.agencedirectserver.utils.CreateClientUtil;
import stage.agencedirectserver.utils.ExpireDateUtil;
import stage.agencedirectserver.utils.UpdateClientUtils;
import stage.agencedirectserver.utils.affectation.ClientToAgenceUtil;
import stage.agencedirectserver.utils.affectation.ClientToPackUtil;
import stage.agencedirectserver.utils.sendmail.EmailDetails;
import stage.agencedirectserver.utils.sendmail.EmailService;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j @Qualifier("ClientService")
//@Primary @Component("ClientService")
public class ClientService{
    private final ClientRepository clientRepository;
    private final AgenceRepository agenceRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AgentService agentService;

    private final EmailService emailService;

    private final PackRepository packRepository;

    // get Methods
    public List<Client> getAllClients() { return clientRepository.findAll();  }
    public Client getClientByEmail(String email) { return clientRepository.findByEmail(email); }
    public Client getClientByCIN(String cin) { return clientRepository.findByCIN(cin); }

    // add Methods
    public Client addClient(Client client) throws MessagingException, NotFoundException {

        CreateClientUtil.create(client,roleRepository,agenceRepository,packRepository,passwordEncoder,emailService,agentService);
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
