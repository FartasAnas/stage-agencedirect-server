package stage.agencedirectserver.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.PackRepository;
import stage.agencedirectserver.repositories.RoleRepository;
import stage.agencedirectserver.services.AgentService;
import stage.agencedirectserver.utils.affectation.ClientToAgenceUtil;
import stage.agencedirectserver.utils.affectation.ClientToPackUtil;
import stage.agencedirectserver.utils.sendmail.EmailDetails;
import stage.agencedirectserver.utils.sendmail.EmailService;

@Slf4j
public class CreateClientUtil {
    public static void create(Client client, RoleRepository roleRepository, AgenceRepository agenceRepository, PackRepository packRepository, PasswordEncoder passwordEncoder, EmailService emailService, AgentService agentService) throws NotFoundException, EmailNotValidException {
        client.getRoles().add(roleRepository.findByName("ROLE_CLIENT"));

        //Email Validation
        if(EmailValidationUtil.isValid(client.getEmail())) {
            log.info("Email is valid");
        } else {
            log.info("Email is not valid");
            throw new EmailNotValidException();
        }

        //Generate a Random 8 character String
        client.setCodeAccess(CodeAccessGenerator.generate());
        log.info("CodeAccess: {}", client.getCodeAccess());
        //agentService.addAgent(new Agent(null,client.getPrenom(),client.getNom(),client.getEmail(),client.getCodeAccess(),client.getAgence(),client.getRoles()));

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

    }

}
