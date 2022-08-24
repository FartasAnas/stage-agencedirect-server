package stage.agencedirectserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.*;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.ClientRepository;
import stage.agencedirectserver.repositories.PackRepository;
import stage.agencedirectserver.repositories.RoleRepository;
import stage.agencedirectserver.utils.ClientGenerateTokenUtil;
import stage.agencedirectserver.utils.CreateClientUtil;
import stage.agencedirectserver.utils.UpdateClientUtils;
import stage.agencedirectserver.utils.sendmail.EmailService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    public Long getClientCount(){ return clientRepository.count(); }
    public Long getDemandeCount(){
        long count = 0;
        for (Client client : clientRepository.findAll()) {
            log.info("Client {}",client.getEmail());
            log.info("isActive {}",client.isActive());
            if(client.isActive() == false)
                count++;
            }
        return count;
    }

    // add Methods
    public Client addClient(Client client) throws MessagingException, NotFoundException, EmailNotValidException {
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
        Client client = clientRepository.findByEmail(email.toLowerCase());
        Agence agence = agenceRepository.findByNom(agenceName.toLowerCase());
        if(client == null || agence == null) {
            throw new NotFoundException("Client or Agence was not found");
        } else if (client.getAgence() != null) {
            throw new DataIntegrityViolationException("Client already has an agence");
        }
        client.setAgence(agence);
    }

    public void addClientToPack(String email,String packName) throws NotFoundException {
        Client client = clientRepository.findByEmail(email.toLowerCase());
        Pack pack = packRepository.findByNom(packName.toLowerCase());
        if(client == null || pack == null) {
            throw new NotFoundException("Client was not found");
        } else if (client.getPack()!=null) {
            throw new DataIntegrityViolationException("Client already has a pack");
        }
        client.setPack(pack);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String email = decodedJWT.getSubject();
            Client client = getClientByEmail(email);
            ClientGenerateTokenUtil.generateClientToken(request, response, client, refreshToken,algorithm);
        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    }


}
