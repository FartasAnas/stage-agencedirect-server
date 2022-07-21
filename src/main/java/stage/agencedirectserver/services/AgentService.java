package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.repositories.AgentRepository;
import stage.agencedirectserver.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class AgentService {
    private final AgentRepository agentRepository;
    private final RoleRepository roleRepository;


    // get Methods
    public List<Agent> getAllAgents() {
        log.info("Fetching all agents");
        return agentRepository.findAll();
    }
    public Agent getAgent(Long id) {
        log.info("Fetching agent with id", id);
        try {
            return agentRepository.findById(id).orElseThrow(null);
        }
        catch (Exception e){
            return null;
        }

    }
    public Agent getAgentByUsername(String username) {
        log.info("Fetching agent ",username);
        return agentRepository.findByUsername(username);
    }

    // post Methods
    public Agent addAgent(Agent agent) {
        log.info("Saving new user ",agent.getUsername() ," to database");
        return agentRepository.save(agent);
    }

    // put Methods
    public Agent updateAgent(Long id, Agent agent) {
        Agent agentToUpdate = agentRepository.findById(id).orElseThrow(null);
        log.info("updating agent ",agentToUpdate.getUsername());

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
    public void addRoleToAgent(String username, String rolename) {
        Agent agent = agentRepository.findByUsername(username);
        Role role = roleRepository.findByName(rolename);

        log.info("adding role ", role.getName() ," to user ",agent.getUsername());
        agent.getRoles().add(role);
    }

}
