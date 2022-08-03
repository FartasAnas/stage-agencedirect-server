package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.AgentRepository;
import stage.agencedirectserver.repositories.RoleRepository;
import stage.agencedirectserver.utils.affectation.AgentToAgence;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
//@Primary @Component("AgentService")
public class AgentService{
    private final AgentRepository agentRepository;
    private final RoleRepository roleRepository;
    private final AgenceRepository agenceRepository;

    private final PasswordEncoder passwordEncoder;


    // get Methods
    public List<Agent> getAllAgents() {
        log.info("Fetching all agents");
        return agentRepository.findAll();
    }
    public Agent getAgent(Long id) throws NotFoundException {
        log.info("Fetching agent with id", id);
        return agentRepository.findById(id).orElseThrow(() -> new NotFoundException("Agent was not found"));
    }
    public Agent getAgentByUsername(String username) {
        log.info("Fetching agent {}",username);
        return agentRepository.findByUsername(username);
    }

    // post Methods
    public Agent addAgent(Agent agent) throws NotFoundException {
        log.info("Saving new user {}",agent.getUsername() ," to database");
        agent.getRoles().add(roleRepository.findByName("ROLE_AGENT"));
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        if(agent.getAgence()!=null)
            agent.setAgence(AgentToAgence.affectAgence(agent,agenceRepository));
        return agentRepository.save(agent);
    }

    // put Methods
    public Agent updateAgent(Long id, Agent agent) throws NotFoundException {
        Agent agentToUpdate = agentRepository.findById(id).orElseThrow(() -> new NotFoundException("Agent was not found"));
        log.info("updating agent {}",agentToUpdate.getUsername());

        agentToUpdate.setPrenom( agent.getPrenom()!=null ? agent.getPrenom() : agentToUpdate.getPrenom() );
        agentToUpdate.setNom( agent.getNom()!=null ? agent.getNom() : agentToUpdate.getNom());
        agentToUpdate.setUsername( agent.getUsername()!=null ? agent.getUsername() : agentToUpdate.getUsername());
        agentToUpdate.setPassword(agent.getPassword()!=null ? agent.getPassword() : agentToUpdate.getPassword());
        return agentToUpdate;
    }

    // delete Methods
    public void deleteAgent(Long id){
        agentRepository.deleteById(id);
    }

    // other Methods
    public void addRoleToAgent(String username, String roleName) throws NotFoundException {
        Agent agent = agentRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        if(agent == null || role == null) {
            throw new NotFoundException("Agent or role not found");
        }
        else if(agent.getRoles().contains(role)) {
            throw new NotFoundException("Agent already has this role");
        }
        log.info("adding role {}", role.getName() ," to user {}",agent.getUsername());
        agent.getRoles().add(role);
    }


}
